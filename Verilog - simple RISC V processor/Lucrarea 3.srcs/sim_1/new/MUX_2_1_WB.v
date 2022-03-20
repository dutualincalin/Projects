`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 12/31/2021 07:49:29 PM
// Design Name: 
// Module Name: MUX_2_1_WB
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


module WB(data, adress, MemtoReg, out);
    input [31:0] data, adress;
    input MemtoReg;
    output [31:0] out;
    
    assign out = (MemtoReg == 1'b1) ? data : adress;
    
endmodule
