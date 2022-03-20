`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 01/11/2022 10:51:21 PM
// Design Name: 
// Module Name: MEM
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


module MEM(clk, mem_read, mem_write, Address, WriteData, Branch, Zero, ReadData, PCSrc);
    input [31:0] Address, WriteData;
    input clk, mem_read, mem_write, Branch, Zero;
    output [31:0] ReadData;
    output PCSrc;
    
    ADD MEM_and(Branch, Zero, PCSrc);
    data_memory MEM_data(clk, mem_read, mem_write, Address, WriteData, ReadData);
endmodule
