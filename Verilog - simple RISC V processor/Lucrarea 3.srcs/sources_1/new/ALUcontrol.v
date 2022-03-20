module ALUcontrol(ALUop, funct7, funct3, ALUinput);
    input [1:0] ALUop;
    input [2:0] funct3;
    input [6:0] funct7;
    output reg [3:0] ALUinput;

    always @(*) begin
        if(ALUop == 2'b0)
            ALUinput <= 4'b0010;
        
        else if(ALUop == 2'b01)
            ALUinput <= 4'b0110;
        
        else if(ALUop == 2'b10) begin
            if(funct7 == 7'b0100000 && funct3 == 3'b0)
               ALUinput <= 4'b0110;
               
            else if(funct7 == 7'b0) begin
                if(funct3 == 3'b0)
                    ALUinput <= 4'b0010;
                
                else if(funct3 == 3'b111)
                    ALUinput <= 4'b0000;
                
                else if(funct3 == 3'b110)
                    ALUinput <= 4'b0001;
            end
        end
        
        else if(ALUop == 2'b11) begin
            if(funct7 == 7'b0100000 && funct3 == 3'b0)
               ALUinput <= 4'b0110;
               
            else if(funct7 == 7'b0) begin
                if(funct3 == 3'b0)
                    ALUinput <= 4'b0010;
                
                else if(funct3 == 3'b111)
                    ALUinput <= 4'b0000;
                
                else if(funct3 == 3'b110)
                    ALUinput <= 4'b0001;
            end
        end
    end
endmodule
    
