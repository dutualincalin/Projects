`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 12/31/2021 01:56:29 PM
// Design Name: 
// Module Name: EX_MEM_pipe
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


module EX_MEM_pipe(clk, reset, ALU_OUT_in, PC_Branch_in, REG_DATA2_in, RD_in, ZERO_in, RegWrite_in, MemtoReg_in, MemWrite_in, MemRead_in, Branch_in,
                    ALU_OUT_out, PC_Branch_out, REG_DATA2_out, RD_out, ZERO_out, RegWrite_out, MemtoReg_out, MemWrite_out, MemRead_out, Branch_out);
    
    input [31:0] ALU_OUT_in, PC_Branch_in, REG_DATA2_in;
    input [4:0] RD_in;
    input clk, reset, ZERO_in, RegWrite_in, MemtoReg_in, MemWrite_in, MemRead_in, Branch_in;
    output reg [31:0] ALU_OUT_out, PC_Branch_out, REG_DATA2_out;
    output reg [4:0] RD_out;
    output reg ZERO_out, RegWrite_out, MemtoReg_out, MemWrite_out, MemRead_out, Branch_out;
    
    always @(posedge clk) begin
        if(reset) begin
            ALU_OUT_out <= 32'b0;
            PC_Branch_out <= 32'b0;
            REG_DATA2_out <= 32'b0;
            RD_out <= 5'b0;
            ZERO_out = 0;
            RegWrite_out = 0;
            MemtoReg_out = 0;
            MemWrite_out = 0;
            MemRead_out = 0;
            Branch_out = 0;
        end
        
        else begin
            ALU_OUT_out <= ALU_OUT_in;
            PC_Branch_out <= PC_Branch_in;
            REG_DATA2_out <= REG_DATA2_in;
            RD_out <= RD_in;
            ZERO_out <= ZERO_in;
            RegWrite_out <= RegWrite_in;
            MemtoReg_out <= MemtoReg_in;
            MemWrite_out <= MemWrite_in;
            MemRead_out <= MemRead_in;
            Branch_out <= Branch_in;
        end
    end
endmodule
