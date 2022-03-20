`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 12/31/2021 01:56:29 PM
// Design Name: 
// Module Name: hazard_detection
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


module hazard_detection(rd, rs1, rs2, MemRead, PCwrite, IF_IDwrite, control_sel);
    input [4:0] rs1, rs2, rd;
    input MemRead;
    output reg PCwrite, IF_IDwrite, control_sel;
    
    always @(*)
        if (MemRead && ((rd == rs1) || (rd == rs2))) begin
            PCwrite <= 0;
            IF_IDwrite <= 0;
            control_sel <= 1;
        end
    
        else begin
            PCwrite <= 1;
            IF_IDwrite <= 1;
            control_sel <= 0;
        end
        
endmodule
