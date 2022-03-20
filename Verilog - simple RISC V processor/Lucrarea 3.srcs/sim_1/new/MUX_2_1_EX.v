`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 12/31/2021 04:43:53 PM
// Design Name: 
// Module Name: MUX_2_1_EX
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


module MUX_2_1_EX(MUX_res, IMM_EX, ALUsrc, out);
    input [31:0] MUX_res, IMM_EX;
    input ALUsrc;
    output [31:0] out;
    
    assign out = (ALUsrc == 1'b0) ? MUX_res : IMM_EX;
    
endmodule