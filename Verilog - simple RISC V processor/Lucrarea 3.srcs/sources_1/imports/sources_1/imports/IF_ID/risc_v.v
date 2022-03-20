//////////////////////////////////////////////RISC-V_MODULE///////////////////////////////////////////////////
module RISC_V(input clk, reset,
              
              output [31:0] PC_EX,
              output [31:0] ALU_OUT_EX,
              output [31:0] PC_MEM,
              output [31:0] DATA_MEMORY_MEM,
              output [31:0] ALU_DATA_WB,
              output PCSrc,
              output [1:0] forwardA, forwardB,
              output pipeline_stall
              );
  
  //////////////////////////////////////////Signals////////////////////////////////////////////////////////
  // IF
  wire [31:0] PC_IF, INSTRUCTION_IF;
  wire PC_write;
  
  // ID
  wire [31:0] PC_ID, INSTRUCTION_ID, IMM_ID, REG_DATA1_ID, REG_DATA2_ID;
  wire [6:0] FUNCT7_ID;
  wire [4:0] RD_ID, RS1_ID, RS2_ID;
  wire [2:0] FUNCT3_ID;
  wire [1:0] ALUop_ID;
  wire RegWrite_ID,MemtoReg_ID,MemRead_ID,MemWrite_ID, ALUSrc_ID, Branch_ID;
  
  // EX
  wire [31:0] IMM_EX, REG_DATA1_EX, REG_DATA2_EX, PC_Branch_EX, REG_DATA2_EX_FINAL;
  wire [6:0] FUNCT7_EX;
  wire [4:0] RS1_EX, RS2_EX, RD_EX;
  wire [2:0] FUNCT3_EX;
  wire [1:0] ALUop_EX;
  wire RegWrite_EX, MemtoReg_EX, MemRead_EX, MemWrite_EX, ALUSrc_EX, Branch_EX, ZERO_EX;
  
  // MEM
  wire [31:0] ALU_OUT_MEM, REG_DATA2_MEM, RD_MEM;
  wire ZERO_MEM, RegWrite_MEM, MemtoReg_MEM, MemWrite_MEM, MemRead_MEM, Branch_MEM;
  
  // WB
  wire [31:0] DATA_MEMORY_WB, adress_WB;
  wire [4:0] RD_WB;
  wire RegWrite_WB, MemtoReg_WB;
  
  // Pipes
  wire IF_ID_write;
  
 // Other signals
 
 /////////////////////////////////////IF Module/////////////////////////////////////
 IF instruction_fetch(clk, reset, 
                      PCSrc, PC_write,
                      PC_MEM,
                      PC_IF,INSTRUCTION_IF);
  
  
 ////////////////////////////////////// IF_ID pipeline////////////////////////////////////////////////////
 IF_ID_reg IF_ID_REGISTER(clk,reset,
                          IF_ID_write,
                          PC_IF,INSTRUCTION_IF,
                          PC_ID,INSTRUCTION_ID);
  
  
////////////////////////////////////////ID Module//////////////////////////////////
 ID instruction_decode(clk,
                       PC_ID,INSTRUCTION_ID,
                       RegWrite_WB, pipeline_stall,
                       ALU_DATA_WB,
                       RD_WB,
                       IMM_ID,
                       REG_DATA1_ID, REG_DATA2_ID,
                       FUNCT7_ID, FUNCT3_ID,
                       RD_ID, RS1_ID, RS2_ID,
                       RegWrite_ID,MemtoReg_ID,MemRead_ID,MemWrite_ID,
                       ALUop_ID,
                       ALUSrc_ID,
                       Branch_ID);
 
////////////////////////////////////////ID_EX pipeline//////////////////////////////////
 ID_EX_pipe ID_EX_REGISTER(clk, reset, 
                            IMM_ID,
                            REG_DATA1_ID, REG_DATA2_ID,
                            PC_ID,
                            FUNCT7_ID, FUNCT3_ID, 
                            RegWrite_ID, MemtoReg_ID, MemRead_ID, MemWrite_ID,
                            RS1_ID, RS2_ID, RD_ID,
                            ALUop_ID, ALUSrc_ID, Branch_ID, 
                            IMM_EX,
                            REG_DATA1_EX, REG_DATA2_EX,
                            PC_EX,
                            FUNCT7_EX, FUNCT3_EX,
                            RS1_EX, RS2_EX, RD_EX,
                            RegWrite_EX, MemtoReg_EX, MemRead_EX, MemWrite_EX, 
                            ALUop_EX, ALUSrc_EX, Branch_EX);
                            
////////////////////////////////////////EX module//////////////////////////////////
 EX instruction_execute(IMM_EX,
                        REG_DATA1_EX, REG_DATA2_EX,
                        PC_EX,
                        FUNCT3_EX, FUNCT7_EX,
                        RD_EX, RS1_EX, RS2_EX,
                        RegWrite_EX, MemtoReg_EX, MemRead_EX, MemWrite_EX,
                        ALUop_EX, ALUSrc_EX, Branch_EX,
                        forwardA, forwardB,
                        ALU_DATA_WB, ALU_OUT_MEM,
                        ZERO_EX,
                        ALU_OUT_EX, PC_Branch_EX, REG_DATA2_EX_FINAL);

////////////////////////////////////////EX_MEM pipeline//////////////////////////////////
 EX_MEM_pipe EX_MEM_REGISTER(clk, reset,
                            ALU_OUT_EX,
                            PC_Branch_EX,
                            REG_DATA2_EX_FINAL,
                            RD_EX,
                            ZERO_EX, RegWrite_EX, MemtoReg_EX, MemWrite_EX, MemRead_EX, Branch_EX,
                            ALU_OUT_MEM, PC_MEM, REG_DATA2_MEM, RD_MEM,
                            ZERO_MEM, RegWrite_MEM, MemtoReg_MEM, MemWrite_MEM, MemRead_MEM, Branch_MEM);
 
////////////////////////////////////////MEM module//////////////////////////////////
 MEM instruction_memory(clk,
                        MemRead_MEM, MemWrite_MEM,
                        ALU_OUT_MEM, REG_DATA2_MEM,
                        Branch_MEM, ZERO_MEM, DATA_MEMORY_MEM, PCSrc);

////////////////////////////////////////MEM_WB pipeline//////////////////////////////////
 MEM_WB_pipe MEM_WB_REGISTER(clk, reset,
                            DATA_MEMORY_MEM, ALU_OUT_MEM,
                            RD_MEM,
                            RegWrite_MEM, MemtoReg_MEM,
                            DATA_MEMORY_WB, adress_WB,
                            RD_WB,
                            RegWrite_WB, MemtoReg_WB);
 
////////////////////////////////////////WB module//////////////////////////////////
 WB instruction_write_back(DATA_MEMORY_WB, adress_WB, MemtoReg_WB, ALU_DATA_WB);

////////////////////////////////////////Forwarding module///////////////////////////////////
 forwarding instruction_forwarding(RS1_EX, RS2_EX, RD_MEM, RD_WB, RegWrite_MEM, RegWrite_WB, forwardA, forwardB);
 
////////////////////////////////////////Hazard Detection module///////////////////////////////////
 hazard_detection hazard_instruction_detection (RD_EX, RS1_ID, RS2_ID, MemRead_EX, PC_write, IF_ID_write, pipeline_stall);

endmodule
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
