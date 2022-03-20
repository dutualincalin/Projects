`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 12/31/2021 01:52:25 PM
// Design Name: 
// Module Name: ALU
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


module ALU(ALUop, ina, inb, zero, out);
    input [3:0] ALUop;
    input [31:0] ina, inb;
    output zero;
    output reg [31:0] out;
    
    reg zero_reg;
    
    always @(*) begin
        if(ALUop == 4'b0)
            out <= ina & inb;
            
        else if(ALUop == 4'b0001)
            out <= ina | inb;
            
        else if(ALUop == 4'b0010)
            out <= ina + inb;
    
        else if(ALUop == 4'b0110)
            out <= ina - inb;
    
        if(out == 32'b0)
            zero_reg = 1;
            
        else zero_reg = 0;
    end
    
    assign zero = zero_reg;
endmodule
