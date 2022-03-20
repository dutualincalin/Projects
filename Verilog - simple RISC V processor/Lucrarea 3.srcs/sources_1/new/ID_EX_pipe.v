`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 12/31/2021 08:06:41 PM
// Design Name: 
// Module Name: ID_EX_pipe
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


module ID_EX_pipe(clk, reset, 
                IMM_in, REG_DATA1_in, REG_DATA2_in, PC_in, FUNCT7_in, FUNCT3_in, RegWrite_in, MemtoReg_in, MemRead_in, MemWrite_in, RS1_in, RS2_in, RD_in, ALUop_in, ALUSrc_in, Branch_in, 
                IMM_out, REG_DATA1_out, REG_DATA2_out, PC_out, FUNCT7_out, FUNCT3_out, RS1_out, RS2_out, RD_out, RegWrite_out, MemtoReg_out, MemRead_out, MemWrite_out, ALUop_out, ALUSrc_out, Branch_out);
    input clk, reset;
    input [31:0] IMM_in, REG_DATA1_in, REG_DATA2_in, PC_in;
    input [6:0] FUNCT7_in;
    input [4:0] RS1_in, RS2_in, RD_in;
    input [2:0] FUNCT3_in;
    input [1:0] ALUop_in;
    input RegWrite_in, MemtoReg_in, MemRead_in, MemWrite_in, ALUSrc_in, Branch_in;
    output reg [31:0] IMM_out, REG_DATA1_out, REG_DATA2_out, PC_out;
    output reg [6:0] FUNCT7_out;
    output reg [4:0] RS1_out, RS2_out, RD_out;
    output reg [2:0] FUNCT3_out;
    output reg [1:0] ALUop_out;
    output reg RegWrite_out, MemtoReg_out, MemRead_out, MemWrite_out, ALUSrc_out, Branch_out;
    
    always @(posedge clk) begin
        if (reset) begin
            IMM_out <= 32'b0;
            REG_DATA1_out <= 32'b0;
            REG_DATA2_out <= 32'b0;
            PC_out <= 32'b0;
            FUNCT7_out <= 7'b0;
            FUNCT3_out <= 3'b0;
            RS1_out <= 5'b0;
            RS2_out <= 5'b0;
            RD_out <= 5'b0;
            ALUop_out <= 2'b0;
            RegWrite_out = 0;
            MemtoReg_out = 0;
            MemRead_out = 0;
            MemWrite_out = 0;
            ALUSrc_out = 0;
            Branch_out = 0;
        end
        
        else begin
            IMM_out <= IMM_in;
            REG_DATA1_out <= REG_DATA1_in;
            REG_DATA2_out <= REG_DATA2_in;
            PC_out <= PC_in;
            FUNCT7_out <= FUNCT7_in;
            FUNCT3_out <= FUNCT3_in;
            RS1_out <= RS1_in;
            RS2_out <= RS2_in;
            RD_out <= RD_in;
            ALUop_out <= ALUop_in;
            RegWrite_out = RegWrite_in;
            MemtoReg_out = MemtoReg_in;
            MemRead_out = MemRead_in;
            MemWrite_out = MemWrite_in;
            ALUSrc_out = ALUSrc_in;
            Branch_out = Branch_in;
        end
    end
        
endmodule
