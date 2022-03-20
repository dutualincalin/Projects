`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 12/31/2021 01:56:29 PM
// Design Name: 
// Module Name: MEM_WB_pipe
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


module MEM_WB_pipe(clk, reset, read_data_in, adress_in, RD_in, RegWrite_in, MemtoReg_in,
                   read_data_out, adress_out, RD_out, RegWrite_out, MemtoReg_out);
                    
    input [31:0] read_data_in, adress_in;
    input [4:0] RD_in;
    input clk, reset, MemtoReg_in, RegWrite_in;
    output reg [31:0] read_data_out, adress_out;
    output reg [4:0] RD_out;
    output reg MemtoReg_out, RegWrite_out;
    
    always @(posedge clk) begin
        if(reset) begin
            read_data_out <= 32'b0;
            adress_out <= 32'b0;
            RD_out <= 5'b0;
            MemtoReg_out <= 0;
            RegWrite_out <= 0;
        end
        
        else begin
            read_data_out <= read_data_in;
            adress_out <= adress_in;
            RD_out <= RD_in;
            MemtoReg_out <= MemtoReg_in;
            RegWrite_out <= RegWrite_in;
        end
    end
endmodule
