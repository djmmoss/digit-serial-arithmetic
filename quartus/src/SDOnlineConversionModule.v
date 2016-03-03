module MuxLeftShiftModule(
    input [14:0] io_in,
    input [14:0] io_shiftAmount,
    output[14:0] io_o
);

  wire[14:0] T10;
  wire[45:0] fourShift;
  wire[45:0] T11;
  wire[29:0] threeShift;
  wire[29:0] T12;
  wire[21:0] twoShift;
  wire[21:0] T13;
  wire[17:0] oneShift;
  wire[17:0] T14;
  wire[15:0] zeroShift;
  wire[15:0] T15;
  wire[15:0] T0;
  wire T1;
  wire[17:0] T2;
  wire T3;
  wire[21:0] T4;
  wire T5;
  wire[29:0] T6;
  wire T7;
  wire[45:0] T8;
  wire T9;


  assign io_o = T10;
  assign T10 = fourShift[4'he:1'h0];
  assign fourShift = T9 ? T8 : T11;
  assign T11 = {16'h0, threeShift};
  assign threeShift = T7 ? T6 : T12;
  assign T12 = {8'h0, twoShift};
  assign twoShift = T5 ? T4 : T13;
  assign T13 = {4'h0, oneShift};
  assign oneShift = T3 ? T2 : T14;
  assign T14 = {2'h0, zeroShift};
  assign zeroShift = T1 ? T0 : T15;
  assign T15 = {1'h0, io_in};
  assign T0 = io_in << 1'h1;
  assign T1 = io_shiftAmount[1'h0:1'h0];
  assign T2 = zeroShift << 2'h2;
  assign T3 = io_shiftAmount[1'h1:1'h1];
  assign T4 = oneShift << 3'h4;
  assign T5 = io_shiftAmount[2'h2:2'h2];
  assign T6 = twoShift << 4'h8;
  assign T7 = io_shiftAmount[2'h3:2'h3];
  assign T8 = threeShift << 5'h10;
  assign T9 = io_shiftAmount[3'h4:3'h4];
endmodule

module SDOnlineConversionModule(input clk, input reset,
    input [1:0] io_a,
    input  io_start,
    output[13:0] io_o
);

  wire[14:0] T13;
  wire[13:0] inCounter;
  wire[13:0] T0;
  reg [13:0] counter;
  wire[13:0] T14;
  wire[14:0] nextQ;
  wire[14:0] T1;
  wire[14:0] T2;
  wire[14:0] T3;
  wire[13:0] inQM;
  reg [13:0] qm;
  wire[13:0] T15;
  wire[14:0] T16;
  wire[14:0] nextQM;
  wire[14:0] T4;
  wire[14:0] T5;
  wire[14:0] T6;
  wire[14:0] T7;
  wire[14:0] T8;
  wire[13:0] inQ;
  reg [13:0] q;
  wire[13:0] T17;
  wire[14:0] T18;
  wire negOne;
  wire[14:0] T9;
  wire zero;
  wire T10;
  wire T11;
  wire[14:0] T12;
  wire one;
  wire[13:0] T19;
  wire[14:0] MuxLeftShiftModule_io_o;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    counter = {1{$random}};
    qm = {1{$random}};
    q = {1{$random}};
  end
// synthesis translate_on
`endif

  assign T13 = {1'h0, inCounter};
  assign inCounter = io_start ? 14'hb : T0;
  assign T0 = counter - 14'h1;
  assign T14 = reset ? 14'hc : inCounter;
  assign nextQ = one ? T12 : T1;
  assign T1 = zero ? T9 : T2;
  assign T2 = negOne ? T3 : 15'h0;
  assign T3 = {inQM, 1'h1};
  assign inQM = io_start ? 14'h0 : qm;
  assign T15 = T16[4'hd:1'h0];
  assign T16 = reset ? 15'h0 : nextQM;
  assign nextQM = one ? T8 : T4;
  assign T4 = zero ? T7 : T5;
  assign T5 = negOne ? T6 : 15'h0;
  assign T6 = {inQM, 1'h0};
  assign T7 = {inQM, 1'h1};
  assign T8 = {inQ, 1'h0};
  assign inQ = io_start ? 14'h0 : q;
  assign T17 = T18[4'hd:1'h0];
  assign T18 = reset ? 15'h0 : nextQ;
  assign negOne = io_a == 2'h1;
  assign T9 = {inQ, 1'h0};
  assign zero = T11 & T10;
  assign T10 = one ^ 1'h1;
  assign T11 = negOne ^ 1'h1;
  assign T12 = {inQ, 1'h1};
  assign one = io_a == 2'h2;
  assign io_o = T19;
  assign T19 = MuxLeftShiftModule_io_o[4'hd:1'h0];
  MuxLeftShiftModule MuxLeftShiftModule(
       .io_in( nextQ ),
       .io_shiftAmount( T13 ),
       .io_o( MuxLeftShiftModule_io_o )
  );

  always @(posedge clk) begin
    if(reset) begin
      counter <= 14'hc;
    end else if(io_start) begin
      counter <= 14'hb;
    end else begin
      counter <= T0;
    end
    qm <= T15;
    q <= T17;
  end
endmodule

