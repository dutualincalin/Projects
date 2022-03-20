`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 12/31/2021 04:43:53 PM
// Design Name: 
// Module Name: MUX_3_1_EX
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


module MUX_3_1_EX(REG_DATA, ALU_DATA_WB, OUT_MEM, Forward, out);
    input [31:0] REG_DATA, ALU_DATA_WB, OUT_MEM;
    input [1:0] Forward;
    output [31:0] out;
    
    assign out = (Forward[1] == 1) ? OUT_MEM : (Forward[0] == 1) ? ALU_DATA_WB : REG_DATA;
    
endmodule
