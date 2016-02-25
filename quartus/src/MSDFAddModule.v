module FullAdderModule(
    input  io_a,
    input  io_b,
    input  io_cin,
    output io_o,
    output io_cout
);

  wire T0;
  wire T1;
  wire T2;
  wire T3;
  wire T4;
  wire T5;
  wire T6;


  assign io_cout = T0;
  assign T0 = T2 | T1;
  assign T1 = io_b & io_cin;
  assign T2 = T4 | T3;
  assign T3 = io_a & io_cin;
  assign T4 = io_a & io_b;
  assign io_o = T5;
  assign T5 = T6 ^ io_cin;
  assign T6 = io_a ^ io_b;
endmodule

module MSDFAddModule(input clk, input reset,
    input [1:0] io_a,
    input [1:0] io_b,
    input  io_start,
    output[1:0] io_c
);

  wire T0;
  wire T1;
  reg  R2;
  wire T26;
  wire T3;
  reg [1:0] R4;
  reg  R5;
  wire T27;
  wire T6;
  wire T7;
  wire T8;
  wire T9;
  wire T10;
  reg [1:0] R11;
  wire T12;
  wire T13;
  reg [1:0] R14;
  wire[1:0] T15;
  wire[1:0] T16;
  wire[1:0] T17;
  wire T18;
  wire T19;
  wire T20;
  wire T21;
  reg  R22;
  reg  R23;
  wire T24;
  reg  R25;
  wire T28;
  wire FullAdderModule_io_o;
  wire FullAdderModule_io_cout;
  wire FullAdderModule_1_io_o;
  wire FullAdderModule_1_io_cout;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    R2 = {1{$random}};
    R4 = {1{$random}};
    R5 = {1{$random}};
    R11 = {1{$random}};
    R14 = {1{$random}};
    R22 = {1{$random}};
    R23 = {1{$random}};
    R25 = {1{$random}};
  end
// synthesis translate_on
`endif

  assign T0 = FullAdderModule_io_cout;
  assign T1 = ~ R2;
  assign T26 = reset ? 1'h0 : T3;
  assign T3 = R4[1'h0:1'h0];
  assign T27 = reset ? 1'h0 : FullAdderModule_io_o;
  assign T6 = T7;
  assign T7 = R4[1'h1:1'h1];
  assign T8 = ~ T9;
  assign T9 = T10;
  assign T10 = R11[1'h0:1'h0];
  assign T12 = T13;
  assign T13 = R11[1'h1:1'h1];
  assign io_c = R14;
  assign T15 = T16;
  assign T16 = T17;
  assign T17 = {T24, T18};
  assign T18 = T19;
  assign T19 = R22 ? 1'h0 : T20;
  assign T20 = T21;
  assign T21 = ~ FullAdderModule_1_io_cout;
  assign T24 = R25;
  assign T28 = reset ? 1'h0 : FullAdderModule_1_io_o;
  FullAdderModule FullAdderModule(
       .io_a( T12 ),
       .io_b( T8 ),
       .io_cin( T6 ),
       .io_o( FullAdderModule_io_o ),
       .io_cout( FullAdderModule_io_cout )
  );
  FullAdderModule FullAdderModule_1(
       .io_a( R5 ),
       .io_b( T1 ),
       .io_cin( T0 ),
       .io_o( FullAdderModule_1_io_o ),
       .io_cout( FullAdderModule_1_io_cout )
  );

  always @(posedge clk) begin
    if(reset) begin
      R2 <= 1'h0;
    end else begin
      R2 <= T3;
    end
    R4 <= io_b;
    if(reset) begin
      R5 <= 1'h0;
    end else begin
      R5 <= FullAdderModule_io_o;
    end
    R11 <= io_a;
    R14 <= T15;
    R22 <= R23;
    R23 <= io_start;
    if(reset) begin
      R25 <= 1'h0;
    end else begin
      R25 <= FullAdderModule_1_io_o;
    end
  end
endmodule

