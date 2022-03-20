`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 01/24/2022 04:55:08 PM
// Design Name: 
// Module Name: mux-control-2_1
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


module MUX_CTRL_2_1(
sel, MemRead_Ctrl, MemtoReg_Ctrl, MemWrite_Ctrl, RegWrite_Ctrl, Branch_Ctrl, ALUSrc_Ctrl, ALUop_Ctrl,
MemRead, MemtoReg, MemWrite, RegWrite, Branch, ALUSrc, ALUop);

input sel;
input MemRead_Ctrl, MemtoReg_Ctrl, MemWrite_Ctrl, RegWrite_Ctrl, Branch_Ctrl, ALUSrc_Ctrl;
input [1:0] ALUop_Ctrl;
output MemRead,MemtoReg,MemWrite,RegWrite,Branch,ALUSrc;
output [1:0] ALUop;

assign {ALUSrc, MemtoReg, RegWrite, MemRead, MemWrite, Branch, ALUop} = (sel == 1) ? 8'b00000000 :
    {ALUSrc_Ctrl, MemtoReg_Ctrl, RegWrite_Ctrl, MemRead_Ctrl, MemWrite_Ctrl, Branch_Ctrl, ALUop_Ctrl};
    
endmodule
