`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 12/31/2021 01:56:29 PM
// Design Name: 
// Module Name: EX
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


module EX(IMM_EX, REG_DATA_1_EX, REG_DATA_2_EX, PC_EX, FUNCT3_EX, FUNCT7_EX, RD_EX, RS1_EX, RS2_EX,
        RegWrite_EX, MemtoReg_EX, MemRead_EX, MemWrite_EX, ALUop_EX, ALUSrc_EX, Branch_EX, forwardA, forwardB,
        ALU_DATA_WB, ALU_OUT_MEM,
        ZERO_EX,
        ALU_OUT_EX, PC_Branch_EX, REG_DATA2_EX_FINAL);
        
        input [31:0] IMM_EX, REG_DATA_1_EX, REG_DATA_2_EX, PC_EX, ALU_DATA_WB, ALU_OUT_MEM;
        input [6:0] FUNCT7_EX;
        input [4:0] RD_EX, RS1_EX, RS2_EX;
        input [2:0] FUNCT3_EX;
        input [1:0] ALUop_EX, forwardA, forwardB;
        input RegWrite_EX, MemtoReg_EX, MemRead_EX, MemWrite_EX, ALUSrc_EX, Branch_EX;
        
        output [31:0] ALU_OUT_EX, PC_Branch_EX, REG_DATA2_EX_FINAL;
        output ZERO_EX;
        
        wire[31:0] out_mux_a, out_mux_b;
        wire[3:0] ALUinput;
        
        MUX_3_1_EX MUX_A(REG_DATA_1_EX, ALU_DATA_WB, ALU_OUT_MEM, forwardA, out_mux_a);
        MUX_3_1_EX MUX_B(REG_DATA_2_EX, ALU_DATA_WB, ALU_OUT_MEM, forwardB, REG_DATA2_EX_FINAL);
        MUX_2_1_EX MUX_B2(REG_DATA2_EX_FINAL, IMM_EX, ALUSrc_EX, out_mux_b);
        
        ALUcontrol ALUop_gen(ALUop_EX, FUNCT7_EX, FUNCT3_EX, ALUinput);
        
        Adder_EX adder(PC_EX, IMM_EX, PC_Branch_EX);
        
        ALU ALU(ALUinput, out_mux_a, out_mux_b, ZERO_EX, ALU_OUT_EX);
        
endmodule
