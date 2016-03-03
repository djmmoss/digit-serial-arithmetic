module FixedDotProductModule(input clk,
    input [31:0] io_a_7,
    input [31:0] io_a_6,
    input [31:0] io_a_5,
    input [31:0] io_a_4,
    input [31:0] io_a_3,
    input [31:0] io_a_2,
    input [31:0] io_a_1,
    input [31:0] io_a_0,
    input [31:0] io_b_7,
    input [31:0] io_b_6,
    input [31:0] io_b_5,
    input [31:0] io_b_4,
    input [31:0] io_b_3,
    input [31:0] io_b_2,
    input [31:0] io_b_1,
    input [31:0] io_b_0,
    output[31:0] io_c
);

  wire[31:0] T0;
  wire[31:0] T80;
  wire[47:0] T1;
  wire[63:0] T2;
  reg [63:0] R3;
  wire[63:0] T4;
  wire[63:0] T5;
  wire[63:0] T6;
  wire[63:0] T7;
  wire[63:0] T8;
  wire[31:0] T9;
  reg [31:0] R10;
  wire[31:0] T11;
  reg [31:0] R12;
  wire[63:0] T13;
  wire[63:0] T14;
  wire[63:0] T15;
  wire[63:0] T16;
  wire[63:0] T17;
  wire[63:0] T18;
  wire[31:0] T19;
  reg [31:0] R20;
  wire[31:0] T21;
  reg [31:0] R22;
  wire[63:0] T23;
  wire[63:0] T24;
  wire[63:0] T25;
  wire[63:0] T26;
  wire[63:0] T27;
  wire[63:0] T28;
  wire[31:0] T29;
  reg [31:0] R30;
  wire[31:0] T31;
  reg [31:0] R32;
  wire[63:0] T33;
  wire[63:0] T34;
  wire[63:0] T35;
  wire[63:0] T36;
  wire[63:0] T37;
  wire[63:0] T38;
  wire[31:0] T39;
  reg [31:0] R40;
  wire[31:0] T41;
  reg [31:0] R42;
  wire[63:0] T43;
  wire[63:0] T44;
  wire[63:0] T45;
  wire[63:0] T46;
  wire[63:0] T47;
  wire[63:0] T48;
  wire[31:0] T49;
  reg [31:0] R50;
  wire[31:0] T51;
  reg [31:0] R52;
  wire[63:0] T53;
  wire[63:0] T54;
  wire[63:0] T55;
  wire[63:0] T56;
  wire[63:0] T57;
  wire[63:0] T58;
  wire[31:0] T59;
  reg [31:0] R60;
  wire[31:0] T61;
  reg [31:0] R62;
  wire[63:0] T63;
  wire[63:0] T64;
  wire[63:0] T65;
  wire[63:0] T66;
  wire[63:0] T67;
  wire[63:0] T68;
  wire[31:0] T69;
  reg [31:0] R70;
  wire[31:0] T71;
  reg [31:0] R72;
  wire[63:0] T73;
  wire[63:0] T74;
  wire[63:0] T75;
  wire[31:0] T76;
  reg [31:0] R77;
  wire[31:0] T78;
  reg [31:0] R79;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    R3 = {2{$random}};
    R10 = {1{$random}};
    R12 = {1{$random}};
    R20 = {1{$random}};
    R22 = {1{$random}};
    R30 = {1{$random}};
    R32 = {1{$random}};
    R40 = {1{$random}};
    R42 = {1{$random}};
    R50 = {1{$random}};
    R52 = {1{$random}};
    R60 = {1{$random}};
    R62 = {1{$random}};
    R70 = {1{$random}};
    R72 = {1{$random}};
    R77 = {1{$random}};
    R79 = {1{$random}};
  end
// synthesis translate_on
`endif

  assign io_c = T0;
  assign T0 = T80;
  assign T80 = T1[5'h1f:1'h0];
  assign T1 = $signed(T2) >>> 5'h10;
  assign T2 = R3;
  assign T4 = T5;
  assign T5 = T13 + T6;
  assign T6 = T7;
  assign T7 = T8;
  assign T8 = $signed(T11) * $signed(T9);
  assign T9 = R10;
  assign T11 = R12;
  assign T13 = T14;
  assign T14 = T15;
  assign T15 = T23 + T16;
  assign T16 = T17;
  assign T17 = T18;
  assign T18 = $signed(T21) * $signed(T19);
  assign T19 = R20;
  assign T21 = R22;
  assign T23 = T24;
  assign T24 = T25;
  assign T25 = T33 + T26;
  assign T26 = T27;
  assign T27 = T28;
  assign T28 = $signed(T31) * $signed(T29);
  assign T29 = R30;
  assign T31 = R32;
  assign T33 = T34;
  assign T34 = T35;
  assign T35 = T43 + T36;
  assign T36 = T37;
  assign T37 = T38;
  assign T38 = $signed(T41) * $signed(T39);
  assign T39 = R40;
  assign T41 = R42;
  assign T43 = T44;
  assign T44 = T45;
  assign T45 = T53 + T46;
  assign T46 = T47;
  assign T47 = T48;
  assign T48 = $signed(T51) * $signed(T49);
  assign T49 = R50;
  assign T51 = R52;
  assign T53 = T54;
  assign T54 = T55;
  assign T55 = T63 + T56;
  assign T56 = T57;
  assign T57 = T58;
  assign T58 = $signed(T61) * $signed(T59);
  assign T59 = R60;
  assign T61 = R62;
  assign T63 = T64;
  assign T64 = T65;
  assign T65 = T73 + T66;
  assign T66 = T67;
  assign T67 = T68;
  assign T68 = $signed(T71) * $signed(T69);
  assign T69 = R70;
  assign T71 = R72;
  assign T73 = T74;
  assign T74 = T75;
  assign T75 = $signed(T78) * $signed(T76);
  assign T76 = R77;
  assign T78 = R79;

  always @(posedge clk) begin
    R3 <= T4;
    R10 <= io_b_7;
    R12 <= io_a_7;
    R20 <= io_b_6;
    R22 <= io_a_6;
    R30 <= io_b_5;
    R32 <= io_a_5;
    R40 <= io_b_4;
    R42 <= io_a_4;
    R50 <= io_b_3;
    R52 <= io_a_3;
    R60 <= io_b_2;
    R62 <= io_a_2;
    R70 <= io_b_1;
    R72 <= io_a_1;
    R77 <= io_b_0;
    R79 <= io_a_0;
  end
endmodule

