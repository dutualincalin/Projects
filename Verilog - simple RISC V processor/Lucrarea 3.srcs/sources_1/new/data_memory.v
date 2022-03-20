`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 12/31/2021 01:56:29 PM
// Design Name: 
// Module Name: data_memory
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


module data_memory(clk, mem_read, mem_write, address, write_data, read_data);
    input [31:0] address, write_data;
    input mem_read, mem_write, clk;
    output reg [31:0] read_data;
    
    reg [31:0] memory_data [0:1023];
    integer i;
    
    initial begin
        for(i = 0; i < 1024; i = i + 1) begin
            memory_data[i] = 0;
        end
    end
    
    always @(address)
        if(mem_read)
            read_data <= memory_data[address>>2];
    
    always @(posedge clk)
        if(mem_write)
            memory_data[address>>2] <= write_data;
endmodule
