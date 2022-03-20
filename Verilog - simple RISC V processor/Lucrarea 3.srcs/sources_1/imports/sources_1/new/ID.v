module ID(input clk,
          input [31:0] PC_ID,INSTRUCTION_ID,
          input RegWrite_WB, control_sel,
          input [31:0] ALU_DATA_WB,
          input [4:0] RD_WB,
          output [31:0] IMM_ID,
          output [31:0] REG_DATA1_ID,REG_DATA2_ID,
          output [6:0] FUNCT7_ID,
          output [2:0] FUNCT3_ID,
          output [4:0] RD_ID, RS1_ID, RS2_ID,
          output RegWrite_ID,MemtoReg_ID,MemRead_ID,MemWrite_ID,
          output [1:0] ALUop_ID,
          output ALUSrc_ID,
          output Branch_ID);

  wire [6:0] OPCODE; assign OPCODE = INSTRUCTION_ID[6:0];
  wire MemRead_Ctrl, MemtoReg_Ctrl, MemWrite_Ctrl, RegWrite_Ctrl, Branch_Ctrl, ALUSrc_Ctrl;
  wire [1:0] ALUop_Ctrl;

  assign FUNCT3_ID = INSTRUCTION_ID[14:12];
  assign FUNCT7_ID = INSTRUCTION_ID[31:25];
  assign RD_ID = INSTRUCTION_ID[11:7];
  assign RS1_ID = INSTRUCTION_ID[19:15];
  assign RS2_ID = INSTRUCTION_ID[24:20];

  control_path CONTROL_PATH_MODULE(OPCODE,         
                                   Branch_Ctrl, MemRead_Ctrl, MemtoReg_Ctrl,
                                   ALUop_Ctrl, MemWrite_Ctrl ,ALUSrc_Ctrl, RegWrite_Ctrl);
                                   
 MUX_CTRL_2_1 CONTROL_MUX(control_sel , MemRead_Ctrl, MemtoReg_Ctrl, MemWrite_Ctrl, RegWrite_Ctrl, Branch_Ctrl, ALUSrc_Ctrl, ALUop_Ctrl,
MemRead_ID, MemtoReg_ID, MemWrite_ID, RegWrite_ID, Branch_ID, ALUSrc_ID, ALUop_ID);
  
  registers REGISTER_FILE_MODULE(clk,RegWrite_WB, 
                                 RS1_ID,    
                                 RS2_ID,    
                                 RD_WB,     
                                 ALU_DATA_WB,
                                 REG_DATA1_ID,REG_DATA2_ID);
  
  imm_gen IMM_GEN_MODULE(INSTRUCTION_ID,IMM_ID);

endmodule