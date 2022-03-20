`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 12/31/2021 01:56:29 PM
// Design Name: 
// Module Name: forwarding
// Project Name: 
// Target Devices: 
// Tool Versions: 
// Description: 
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module forwarding(rs1, rs2, ex_mem_rd, mem_wb_rd, ex_mem_regwrite, mem_wb_regwrite, forwardA, forwardB);
    input [4:0] rs1, rs2, ex_mem_rd, mem_wb_rd;
    input ex_mem_regwrite, mem_wb_regwrite;
    output reg [1:0] forwardA, forwardB;
    
    always @(*) begin
        if(ex_mem_regwrite && ex_mem_rd != 0 && ex_mem_rd == rs1) begin
            forwardA <= 2'b10;
        end
        
        else if(mem_wb_regwrite && mem_wb_rd != 0 && mem_wb_rd == rs1) begin
            forwardA <= 2'b01;
        end
        
        else forwardA <= 2'b0;
           
            
        if(ex_mem_regwrite && ex_mem_rd != 0 && ex_mem_rd == rs2) begin
            forwardB <= 2'b10;
        end
         
        else if(mem_wb_regwrite && mem_wb_rd != 0 && mem_wb_rd == rs2) begin
            forwardB <= 2'b01;
        end
        
        else forwardB <= 2'b0;
    end
endmodule
