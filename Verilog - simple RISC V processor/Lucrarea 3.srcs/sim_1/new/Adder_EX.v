`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 12/31/2021 04:43:53 PM
// Design Name: 
// Module Name: Adder_EX
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


module Adder_EX(PC_EX, IMM_EX, PC_Branch);
    input [31:0] PC_EX, IMM_EX;
    output [31:0] PC_Branch;
    
    assign PC_Branch = PC_EX + IMM_EX;
    
endmodule
