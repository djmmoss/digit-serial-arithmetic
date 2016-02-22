module MSDFAddModule(input clk, input reset,
    input [1:0] io_a,
    input [1:0] io_b,
    input  io_start,
    output[1:0] io_c
);

  wire[1:0] T0;
  wire[1:0] T1;
  wire[1:0] T2;
  wire T3;
  wire T4;
  wire T5;
  wire T6;
  wire T7;
  wire T8;
  wire T9;
  wire T10;
  wire T11;
  wire T12;
  wire T13;
  wire T14;
  wire T15;
  wire T16;
  wire T17;
  wire T18;
  wire T19;
  wire T20;
  wire T21;
  wire T22;
  reg  R23;
  wire T36;
  wire T24;
  wire T25;
  wire T26;
  reg  R27;
  wire T37;
  wire T28;
  wire T29;
  wire T30;
  reg  R31;
  wire T32;
  reg  R33;
  wire T38;
  wire T34;
  wire T35;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    R23 = {1{$random}};
    R27 = {1{$random}};
    R31 = {1{$random}};
    R33 = {1{$random}};
  end
// synthesis translate_on
`endif

  assign io_c = T0;
  assign T0 = T1;
  assign T1 = T2;
  assign T2 = {T32, T3};
  assign T3 = T4;
  assign T4 = R31 ? 1'h0 : T5;
  assign T5 = T6;
  assign T6 = ~ T7;
  assign T7 = T25 | T8;
  assign T8 = T22 & T9;
  assign T9 = T10;
  assign T10 = T17 | T11;
  assign T11 = T14 & T12;
  assign T12 = T13;
  assign T13 = io_b[1'h1:1'h1];
  assign T14 = ~ T15;
  assign T15 = T16;
  assign T16 = io_a[1'h0:1'h0];
  assign T17 = T21 | T18;
  assign T18 = T19 & T12;
  assign T19 = T20;
  assign T20 = io_a[1'h1:1'h1];
  assign T21 = T19 & T14;
  assign T22 = ~ R23;
  assign T36 = reset ? 1'h0 : T24;
  assign T24 = io_b[1'h0:1'h0];
  assign T25 = T30 | T26;
  assign T26 = R27 & T9;
  assign T37 = reset ? 1'h0 : T28;
  assign T28 = T29 ^ T12;
  assign T29 = T19 ^ T14;
  assign T30 = R27 & T22;
  assign T32 = R33;
  assign T38 = reset ? 1'h0 : T34;
  assign T34 = T35 ^ T9;
  assign T35 = R27 ^ T22;

  always @(posedge clk) begin
    if(reset) begin
      R23 <= 1'h0;
    end else begin
      R23 <= T24;
    end
    if(reset) begin
      R27 <= 1'h0;
    end else begin
      R27 <= T28;
    end
    R31 <= io_start;
    if(reset) begin
      R33 <= 1'h0;
    end else begin
      R33 <= T34;
    end
  end
endmodule

