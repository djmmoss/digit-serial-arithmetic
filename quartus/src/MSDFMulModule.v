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

module fourToTwoAdderModule(
    input [13:0] io_a,
    input [13:0] io_b,
    input [13:0] io_c,
    input [13:0] io_d,
    input  io_cin1,
    input  io_cin2,
    output[13:0] io_o,
    output[13:0] io_cout
);

  wire cT_13;
  wire T0;
  wire T1;
  wire T2;
  wire T3;
  wire cT_12;
  wire T4;
  wire T5;
  wire T6;
  wire T7;
  wire cT_11;
  wire T8;
  wire T9;
  wire T10;
  wire T11;
  wire cT_10;
  wire T12;
  wire T13;
  wire T14;
  wire T15;
  wire cT_9;
  wire T16;
  wire T17;
  wire T18;
  wire T19;
  wire cT_8;
  wire T20;
  wire T21;
  wire T22;
  wire T23;
  wire cT_7;
  wire T24;
  wire T25;
  wire T26;
  wire T27;
  wire cT_6;
  wire T28;
  wire T29;
  wire T30;
  wire T31;
  wire cT_5;
  wire T32;
  wire T33;
  wire T34;
  wire T35;
  wire cT_4;
  wire T36;
  wire T37;
  wire T38;
  wire T39;
  wire cT_3;
  wire T40;
  wire T41;
  wire T42;
  wire T43;
  wire cT_2;
  wire T44;
  wire T45;
  wire T46;
  wire T47;
  wire cT_1;
  wire T48;
  wire T49;
  wire T50;
  wire T51;
  wire cT_0;
  wire T52;
  wire T53;
  wire T54;
  wire T55;
  wire[13:0] T56;
  wire[13:0] T57;
  wire[13:0] T58;
  wire[6:0] T59;
  wire[3:0] T60;
  wire[1:0] T61;
  wire vcF_0;
  wire vc_0;
  wire vcF_1;
  wire vc_1;
  wire[1:0] T62;
  wire vcF_2;
  wire vc_2;
  wire vcF_3;
  wire vc_3;
  wire[2:0] T63;
  wire[1:0] T64;
  wire vcF_4;
  wire vc_4;
  wire vcF_5;
  wire vc_5;
  wire vcF_6;
  wire vc_6;
  wire[6:0] T65;
  wire[3:0] T66;
  wire[1:0] T67;
  wire vcF_7;
  wire vc_7;
  wire vcF_8;
  wire vc_8;
  wire[1:0] T68;
  wire vcF_9;
  wire vc_9;
  wire vcF_10;
  wire vc_10;
  wire[2:0] T69;
  wire[1:0] T70;
  wire vcF_11;
  wire vc_11;
  wire vcF_12;
  wire vc_12;
  wire vcF_13;
  wire vc_13;
  wire[13:0] T71;
  wire[13:0] T72;
  wire[13:0] T73;
  wire[6:0] T74;
  wire[3:0] T75;
  wire[1:0] T76;
  wire vs_0;
  wire vs_1;
  wire[1:0] T77;
  wire vs_2;
  wire vs_3;
  wire[2:0] T78;
  wire[1:0] T79;
  wire vs_4;
  wire vs_5;
  wire vs_6;
  wire[6:0] T80;
  wire[3:0] T81;
  wire[1:0] T82;
  wire vs_7;
  wire vs_8;
  wire[1:0] T83;
  wire vs_9;
  wire vs_10;
  wire[2:0] T84;
  wire[1:0] T85;
  wire vs_11;
  wire vs_12;
  wire vs_13;
  wire FullAdderModule_io_o;
  wire FullAdderModule_io_cout;
  wire FullAdderModule_1_io_o;
  wire FullAdderModule_1_io_cout;
  wire FullAdderModule_2_io_o;
  wire FullAdderModule_2_io_cout;
  wire FullAdderModule_3_io_o;
  wire FullAdderModule_3_io_cout;
  wire FullAdderModule_4_io_o;
  wire FullAdderModule_4_io_cout;
  wire FullAdderModule_5_io_o;
  wire FullAdderModule_5_io_cout;
  wire FullAdderModule_6_io_o;
  wire FullAdderModule_6_io_cout;
  wire FullAdderModule_7_io_o;
  wire FullAdderModule_7_io_cout;
  wire FullAdderModule_8_io_o;
  wire FullAdderModule_8_io_cout;
  wire FullAdderModule_9_io_o;
  wire FullAdderModule_9_io_cout;
  wire FullAdderModule_10_io_o;
  wire FullAdderModule_10_io_cout;
  wire FullAdderModule_11_io_o;
  wire FullAdderModule_11_io_cout;
  wire FullAdderModule_12_io_o;
  wire FullAdderModule_12_io_cout;
  wire FullAdderModule_13_io_o;
  wire FullAdderModule_13_io_cout;
  wire FullAdderModule_14_io_o;
  wire FullAdderModule_14_io_cout;
  wire FullAdderModule_15_io_o;
  wire FullAdderModule_15_io_cout;
  wire FullAdderModule_16_io_o;
  wire FullAdderModule_16_io_cout;
  wire FullAdderModule_17_io_o;
  wire FullAdderModule_17_io_cout;
  wire FullAdderModule_18_io_o;
  wire FullAdderModule_18_io_cout;
  wire FullAdderModule_19_io_o;
  wire FullAdderModule_19_io_cout;
  wire FullAdderModule_20_io_o;
  wire FullAdderModule_20_io_cout;
  wire FullAdderModule_21_io_o;
  wire FullAdderModule_21_io_cout;
  wire FullAdderModule_22_io_o;
  wire FullAdderModule_22_io_cout;
  wire FullAdderModule_23_io_o;
  wire FullAdderModule_23_io_cout;
  wire FullAdderModule_24_io_o;
  wire FullAdderModule_24_io_cout;
  wire FullAdderModule_25_io_o;
  wire FullAdderModule_25_io_cout;
  wire FullAdderModule_26_io_o;
  wire FullAdderModule_27_io_o;


  assign cT_13 = FullAdderModule_24_io_cout;
  assign T0 = io_d[4'hd:4'hd];
  assign T1 = io_c[4'hd:4'hd];
  assign T2 = io_b[4'hd:4'hd];
  assign T3 = io_a[4'hd:4'hd];
  assign cT_12 = FullAdderModule_22_io_cout;
  assign T4 = io_d[4'hc:4'hc];
  assign T5 = io_c[4'hc:4'hc];
  assign T6 = io_b[4'hc:4'hc];
  assign T7 = io_a[4'hc:4'hc];
  assign cT_11 = FullAdderModule_20_io_cout;
  assign T8 = io_d[4'hb:4'hb];
  assign T9 = io_c[4'hb:4'hb];
  assign T10 = io_b[4'hb:4'hb];
  assign T11 = io_a[4'hb:4'hb];
  assign cT_10 = FullAdderModule_18_io_cout;
  assign T12 = io_d[4'ha:4'ha];
  assign T13 = io_c[4'ha:4'ha];
  assign T14 = io_b[4'ha:4'ha];
  assign T15 = io_a[4'ha:4'ha];
  assign cT_9 = FullAdderModule_16_io_cout;
  assign T16 = io_d[4'h9:4'h9];
  assign T17 = io_c[4'h9:4'h9];
  assign T18 = io_b[4'h9:4'h9];
  assign T19 = io_a[4'h9:4'h9];
  assign cT_8 = FullAdderModule_14_io_cout;
  assign T20 = io_d[4'h8:4'h8];
  assign T21 = io_c[4'h8:4'h8];
  assign T22 = io_b[4'h8:4'h8];
  assign T23 = io_a[4'h8:4'h8];
  assign cT_7 = FullAdderModule_12_io_cout;
  assign T24 = io_d[3'h7:3'h7];
  assign T25 = io_c[3'h7:3'h7];
  assign T26 = io_b[3'h7:3'h7];
  assign T27 = io_a[3'h7:3'h7];
  assign cT_6 = FullAdderModule_10_io_cout;
  assign T28 = io_d[3'h6:3'h6];
  assign T29 = io_c[3'h6:3'h6];
  assign T30 = io_b[3'h6:3'h6];
  assign T31 = io_a[3'h6:3'h6];
  assign cT_5 = FullAdderModule_8_io_cout;
  assign T32 = io_d[3'h5:3'h5];
  assign T33 = io_c[3'h5:3'h5];
  assign T34 = io_b[3'h5:3'h5];
  assign T35 = io_a[3'h5:3'h5];
  assign cT_4 = FullAdderModule_6_io_cout;
  assign T36 = io_d[3'h4:3'h4];
  assign T37 = io_c[3'h4:3'h4];
  assign T38 = io_b[3'h4:3'h4];
  assign T39 = io_a[3'h4:3'h4];
  assign cT_3 = FullAdderModule_4_io_cout;
  assign T40 = io_d[2'h3:2'h3];
  assign T41 = io_c[2'h3:2'h3];
  assign T42 = io_b[2'h3:2'h3];
  assign T43 = io_a[2'h3:2'h3];
  assign cT_2 = FullAdderModule_2_io_cout;
  assign T44 = io_d[2'h2:2'h2];
  assign T45 = io_c[2'h2:2'h2];
  assign T46 = io_b[2'h2:2'h2];
  assign T47 = io_a[2'h2:2'h2];
  assign cT_1 = FullAdderModule_io_cout;
  assign T48 = io_d[1'h1:1'h1];
  assign T49 = io_c[1'h1:1'h1];
  assign T50 = io_b[1'h1:1'h1];
  assign T51 = io_a[1'h1:1'h1];
  assign cT_0 = io_cin2;
  assign T52 = io_d[1'h0:1'h0];
  assign T53 = io_c[1'h0:1'h0];
  assign T54 = io_b[1'h0:1'h0];
  assign T55 = io_a[1'h0:1'h0];
  assign io_cout = T56;
  assign T56 = T57;
  assign T57 = T58;
  assign T58 = {T65, T59};
  assign T59 = {T63, T60};
  assign T60 = {T62, T61};
  assign T61 = {vcF_1, vcF_0};
  assign vcF_0 = vc_0;
  assign vc_0 = io_cin1;
  assign vcF_1 = vc_1;
  assign vc_1 = FullAdderModule_1_io_cout;
  assign T62 = {vcF_3, vcF_2};
  assign vcF_2 = vc_2;
  assign vc_2 = FullAdderModule_3_io_cout;
  assign vcF_3 = vc_3;
  assign vc_3 = FullAdderModule_5_io_cout;
  assign T63 = {vcF_6, T64};
  assign T64 = {vcF_5, vcF_4};
  assign vcF_4 = vc_4;
  assign vc_4 = FullAdderModule_7_io_cout;
  assign vcF_5 = vc_5;
  assign vc_5 = FullAdderModule_9_io_cout;
  assign vcF_6 = vc_6;
  assign vc_6 = FullAdderModule_11_io_cout;
  assign T65 = {T69, T66};
  assign T66 = {T68, T67};
  assign T67 = {vcF_8, vcF_7};
  assign vcF_7 = vc_7;
  assign vc_7 = FullAdderModule_13_io_cout;
  assign vcF_8 = vc_8;
  assign vc_8 = FullAdderModule_15_io_cout;
  assign T68 = {vcF_10, vcF_9};
  assign vcF_9 = vc_9;
  assign vc_9 = FullAdderModule_17_io_cout;
  assign vcF_10 = vc_10;
  assign vc_10 = FullAdderModule_19_io_cout;
  assign T69 = {vcF_13, T70};
  assign T70 = {vcF_12, vcF_11};
  assign vcF_11 = vc_11;
  assign vc_11 = FullAdderModule_21_io_cout;
  assign vcF_12 = vc_12;
  assign vc_12 = FullAdderModule_23_io_cout;
  assign vcF_13 = vc_13;
  assign vc_13 = FullAdderModule_25_io_cout;
  assign io_o = T71;
  assign T71 = T72;
  assign T72 = T73;
  assign T73 = {T80, T74};
  assign T74 = {T78, T75};
  assign T75 = {T77, T76};
  assign T76 = {vs_1, vs_0};
  assign vs_0 = FullAdderModule_1_io_o;
  assign vs_1 = FullAdderModule_3_io_o;
  assign T77 = {vs_3, vs_2};
  assign vs_2 = FullAdderModule_5_io_o;
  assign vs_3 = FullAdderModule_7_io_o;
  assign T78 = {vs_6, T79};
  assign T79 = {vs_5, vs_4};
  assign vs_4 = FullAdderModule_9_io_o;
  assign vs_5 = FullAdderModule_11_io_o;
  assign vs_6 = FullAdderModule_13_io_o;
  assign T80 = {T84, T81};
  assign T81 = {T83, T82};
  assign T82 = {vs_8, vs_7};
  assign vs_7 = FullAdderModule_15_io_o;
  assign vs_8 = FullAdderModule_17_io_o;
  assign T83 = {vs_10, vs_9};
  assign vs_9 = FullAdderModule_19_io_o;
  assign vs_10 = FullAdderModule_21_io_o;
  assign T84 = {vs_13, T85};
  assign T85 = {vs_12, vs_11};
  assign vs_11 = FullAdderModule_23_io_o;
  assign vs_12 = FullAdderModule_25_io_o;
  assign vs_13 = FullAdderModule_27_io_o;
  FullAdderModule FullAdderModule(
       .io_a( T55 ),
       .io_b( T54 ),
       .io_cin( T53 ),
       .io_o( FullAdderModule_io_o ),
       .io_cout( FullAdderModule_io_cout )
  );
  FullAdderModule FullAdderModule_1(
       .io_a( FullAdderModule_io_o ),
       .io_b( T52 ),
       .io_cin( cT_0 ),
       .io_o( FullAdderModule_1_io_o ),
       .io_cout( FullAdderModule_1_io_cout )
  );
  FullAdderModule FullAdderModule_2(
       .io_a( T51 ),
       .io_b( T50 ),
       .io_cin( T49 ),
       .io_o( FullAdderModule_2_io_o ),
       .io_cout( FullAdderModule_2_io_cout )
  );
  FullAdderModule FullAdderModule_3(
       .io_a( FullAdderModule_2_io_o ),
       .io_b( T48 ),
       .io_cin( cT_1 ),
       .io_o( FullAdderModule_3_io_o ),
       .io_cout( FullAdderModule_3_io_cout )
  );
  FullAdderModule FullAdderModule_4(
       .io_a( T47 ),
       .io_b( T46 ),
       .io_cin( T45 ),
       .io_o( FullAdderModule_4_io_o ),
       .io_cout( FullAdderModule_4_io_cout )
  );
  FullAdderModule FullAdderModule_5(
       .io_a( FullAdderModule_4_io_o ),
       .io_b( T44 ),
       .io_cin( cT_2 ),
       .io_o( FullAdderModule_5_io_o ),
       .io_cout( FullAdderModule_5_io_cout )
  );
  FullAdderModule FullAdderModule_6(
       .io_a( T43 ),
       .io_b( T42 ),
       .io_cin( T41 ),
       .io_o( FullAdderModule_6_io_o ),
       .io_cout( FullAdderModule_6_io_cout )
  );
  FullAdderModule FullAdderModule_7(
       .io_a( FullAdderModule_6_io_o ),
       .io_b( T40 ),
       .io_cin( cT_3 ),
       .io_o( FullAdderModule_7_io_o ),
       .io_cout( FullAdderModule_7_io_cout )
  );
  FullAdderModule FullAdderModule_8(
       .io_a( T39 ),
       .io_b( T38 ),
       .io_cin( T37 ),
       .io_o( FullAdderModule_8_io_o ),
       .io_cout( FullAdderModule_8_io_cout )
  );
  FullAdderModule FullAdderModule_9(
       .io_a( FullAdderModule_8_io_o ),
       .io_b( T36 ),
       .io_cin( cT_4 ),
       .io_o( FullAdderModule_9_io_o ),
       .io_cout( FullAdderModule_9_io_cout )
  );
  FullAdderModule FullAdderModule_10(
       .io_a( T35 ),
       .io_b( T34 ),
       .io_cin( T33 ),
       .io_o( FullAdderModule_10_io_o ),
       .io_cout( FullAdderModule_10_io_cout )
  );
  FullAdderModule FullAdderModule_11(
       .io_a( FullAdderModule_10_io_o ),
       .io_b( T32 ),
       .io_cin( cT_5 ),
       .io_o( FullAdderModule_11_io_o ),
       .io_cout( FullAdderModule_11_io_cout )
  );
  FullAdderModule FullAdderModule_12(
       .io_a( T31 ),
       .io_b( T30 ),
       .io_cin( T29 ),
       .io_o( FullAdderModule_12_io_o ),
       .io_cout( FullAdderModule_12_io_cout )
  );
  FullAdderModule FullAdderModule_13(
       .io_a( FullAdderModule_12_io_o ),
       .io_b( T28 ),
       .io_cin( cT_6 ),
       .io_o( FullAdderModule_13_io_o ),
       .io_cout( FullAdderModule_13_io_cout )
  );
  FullAdderModule FullAdderModule_14(
       .io_a( T27 ),
       .io_b( T26 ),
       .io_cin( T25 ),
       .io_o( FullAdderModule_14_io_o ),
       .io_cout( FullAdderModule_14_io_cout )
  );
  FullAdderModule FullAdderModule_15(
       .io_a( FullAdderModule_14_io_o ),
       .io_b( T24 ),
       .io_cin( cT_7 ),
       .io_o( FullAdderModule_15_io_o ),
       .io_cout( FullAdderModule_15_io_cout )
  );
  FullAdderModule FullAdderModule_16(
       .io_a( T23 ),
       .io_b( T22 ),
       .io_cin( T21 ),
       .io_o( FullAdderModule_16_io_o ),
       .io_cout( FullAdderModule_16_io_cout )
  );
  FullAdderModule FullAdderModule_17(
       .io_a( FullAdderModule_16_io_o ),
       .io_b( T20 ),
       .io_cin( cT_8 ),
       .io_o( FullAdderModule_17_io_o ),
       .io_cout( FullAdderModule_17_io_cout )
  );
  FullAdderModule FullAdderModule_18(
       .io_a( T19 ),
       .io_b( T18 ),
       .io_cin( T17 ),
       .io_o( FullAdderModule_18_io_o ),
       .io_cout( FullAdderModule_18_io_cout )
  );
  FullAdderModule FullAdderModule_19(
       .io_a( FullAdderModule_18_io_o ),
       .io_b( T16 ),
       .io_cin( cT_9 ),
       .io_o( FullAdderModule_19_io_o ),
       .io_cout( FullAdderModule_19_io_cout )
  );
  FullAdderModule FullAdderModule_20(
       .io_a( T15 ),
       .io_b( T14 ),
       .io_cin( T13 ),
       .io_o( FullAdderModule_20_io_o ),
       .io_cout( FullAdderModule_20_io_cout )
  );
  FullAdderModule FullAdderModule_21(
       .io_a( FullAdderModule_20_io_o ),
       .io_b( T12 ),
       .io_cin( cT_10 ),
       .io_o( FullAdderModule_21_io_o ),
       .io_cout( FullAdderModule_21_io_cout )
  );
  FullAdderModule FullAdderModule_22(
       .io_a( T11 ),
       .io_b( T10 ),
       .io_cin( T9 ),
       .io_o( FullAdderModule_22_io_o ),
       .io_cout( FullAdderModule_22_io_cout )
  );
  FullAdderModule FullAdderModule_23(
       .io_a( FullAdderModule_22_io_o ),
       .io_b( T8 ),
       .io_cin( cT_11 ),
       .io_o( FullAdderModule_23_io_o ),
       .io_cout( FullAdderModule_23_io_cout )
  );
  FullAdderModule FullAdderModule_24(
       .io_a( T7 ),
       .io_b( T6 ),
       .io_cin( T5 ),
       .io_o( FullAdderModule_24_io_o ),
       .io_cout( FullAdderModule_24_io_cout )
  );
  FullAdderModule FullAdderModule_25(
       .io_a( FullAdderModule_24_io_o ),
       .io_b( T4 ),
       .io_cin( cT_12 ),
       .io_o( FullAdderModule_25_io_o ),
       .io_cout( FullAdderModule_25_io_cout )
  );
  FullAdderModule FullAdderModule_26(
       .io_a( T3 ),
       .io_b( T2 ),
       .io_cin( T1 ),
       .io_o( FullAdderModule_26_io_o )
       //.io_cout(  )
  );
  FullAdderModule FullAdderModule_27(
       .io_a( FullAdderModule_26_io_o ),
       .io_b( T0 ),
       .io_cin( cT_13 ),
       .io_o( FullAdderModule_27_io_o )
       //.io_cout(  )
  );
endmodule

module MulSEL(
    input [2:0] io_a,
    output[1:0] io_o
);

  wire[1:0] T0;
  wire[1:0] T1;
  wire T2;
  wire[1:0] T3;
  wire T4;
  wire T5;
  wire T6;
  wire T7;
  wire T8;
  wire[1:0] T9;
  wire T10;
  wire T11;
  wire T12;
  wire T13;


  assign io_o = T0;
  assign T0 = T8 ? 2'h2 : T1;
  assign T1 = T2 ? 2'h1 : 2'h0;
  assign T2 = T3 == 2'h2;
  assign T3 = {T7, T4};
  assign T4 = T6 & T5;
  assign T5 = io_a[1'h0:1'h0];
  assign T6 = io_a[1'h1:1'h1];
  assign T7 = io_a[2'h2:2'h2];
  assign T8 = T9 == 2'h1;
  assign T9 = {T13, T10};
  assign T10 = T12 | T11;
  assign T11 = io_a[1'h0:1'h0];
  assign T12 = io_a[1'h1:1'h1];
  assign T13 = io_a[2'h2:2'h2];
endmodule

module MSDFMulModule(input clk, input reset,
    input [1:0] io_a,
    input [1:0] io_b,
    input  io_start,
    output[1:0] io_c
);

  wire[2:0] T0;
  wire[3:0] T1;
  wire[3:0] T2;
  wire[3:0] T3;
  wire T4;
  reg [1:0] R5;
  wire T6;
  reg [1:0] R7;
  wire[13:0] T8;
  reg [13:0] R9;
  wire[13:0] T45;
  wire[13:0] T46;
  wire[10:0] T10;
  wire[9:0] T11;
  wire T12;
  reg  R13;
  wire[13:0] T14;
  wire[13:0] T15;
  wire[13:0] T16;
  wire[13:0] T17;
  wire[13:0] T47;
  wire[10:0] T18;
  wire T19;
  wire[13:0] T20;
  wire T21;
  wire[13:0] T22;
  wire[13:0] T23;
  wire[13:0] T24;
  wire[13:0] T25;
  reg [13:0] R26;
  wire[13:0] T48;
  wire[13:0] T49;
  wire[10:0] T27;
  wire T28;
  wire[13:0] T29;
  wire T30;
  wire[13:0] T31;
  reg [13:0] R32;
  wire[13:0] T50;
  wire[14:0] T51;
  wire[14:0] T33;
  wire[13:0] T34;
  wire[13:0] T35;
  wire[11:0] T36;
  wire[9:0] T37;
  wire[1:0] T38;
  wire[1:0] T39;
  wire T40;
  wire T41;
  wire T42;
  wire T43;
  reg [1:0] R44;
  wire[1:0] MulSEL_io_o;
  wire[13:0] SDOnlineConversionModule_io_o;
  wire[13:0] SDOnlineConversionModule_1_io_o;
  wire[13:0] fourToTwoAdderModule_io_o;
  wire[13:0] fourToTwoAdderModule_io_cout;

`ifndef SYNTHESIS
// synthesis translate_off
  integer initvar;
  initial begin
    #0.002;
    R5 = {1{$random}};
    R7 = {1{$random}};
    R9 = {1{$random}};
    R13 = {1{$random}};
    R26 = {1{$random}};
    R32 = {1{$random}};
    R44 = {1{$random}};
  end
// synthesis translate_on
`endif

  assign T0 = T1[2'h3:1'h1];
  assign T1 = T3 + T2;
  assign T2 = fourToTwoAdderModule_io_cout[4'hd:4'ha];
  assign T3 = fourToTwoAdderModule_io_o[4'hd:4'ha];
  assign T4 = R5 == 2'h1;
  assign T6 = R7 == 2'h1;
  assign T8 = T12 ? 14'h0 : R9;
  assign T45 = reset ? 14'h0 : T46;
  assign T46 = {3'h0, T10};
  assign T10 = T11 << 1'h1;
  assign T11 = fourToTwoAdderModule_io_cout[4'h9:1'h0];
  assign T12 = R13;
  assign T14 = T15;
  assign T15 = T21 ? T20 : T16;
  assign T16 = T19 ? T17 : 14'h0;
  assign T17 = T47;
  assign T47 = {3'h0, T18};
  assign T18 = SDOnlineConversionModule_1_io_o >> 2'h3;
  assign T19 = R7 == 2'h2;
  assign T20 = ~ T17;
  assign T21 = R7 == 2'h1;
  assign T22 = T23;
  assign T23 = R13 ? 14'h0 : T24;
  assign T24 = T30 ? T29 : T25;
  assign T25 = T28 ? R26 : 14'h0;
  assign T48 = reset ? 14'h0 : T49;
  assign T49 = {3'h0, T27};
  assign T27 = SDOnlineConversionModule_io_o >> 2'h3;
  assign T28 = R5 == 2'h2;
  assign T29 = ~ R26;
  assign T30 = R5 == 2'h1;
  assign T31 = T12 ? 14'h0 : R32;
  assign T50 = T51[4'hd:1'h0];
  assign T51 = reset ? 15'h0 : T33;
  assign T33 = T34 << 1'h1;
  assign T34 = T35;
  assign T35 = {T39, T36};
  assign T36 = {T38, T37};
  assign T37 = fourToTwoAdderModule_io_o[4'h9:1'h0];
  assign T38 = T1[1'h1:1'h0];
  assign T39 = {1'h1, T40};
  assign T40 = T42 ^ T41;
  assign T41 = T1[2'h2:2'h2];
  assign T42 = T43 == 1'h0;
  assign T43 = MulSEL_io_o == 2'h0;
  assign io_c = R44;
  SDOnlineConversionModule SDOnlineConversionModule(.clk(clk), .reset(reset),
       .io_a( R7 ),
       .io_start( R13 ),
       .io_o( SDOnlineConversionModule_io_o )
  );
  SDOnlineConversionModule SDOnlineConversionModule_1(.clk(clk), .reset(reset),
       .io_a( R5 ),
       .io_start( R13 ),
       .io_o( SDOnlineConversionModule_1_io_o )
  );
  fourToTwoAdderModule fourToTwoAdderModule(
       .io_a( T31 ),
       .io_b( T22 ),
       .io_c( T14 ),
       .io_d( T8 ),
       .io_cin1( T6 ),
       .io_cin2( T4 ),
       .io_o( fourToTwoAdderModule_io_o ),
       .io_cout( fourToTwoAdderModule_io_cout )
  );
  MulSEL MulSEL(
       .io_a( T0 ),
       .io_o( MulSEL_io_o )
  );

  always @(posedge clk) begin
    R5 <= io_b;
    R7 <= io_a;
    if(reset) begin
      R9 <= 14'h0;
    end else begin
      R9 <= T46;
    end
    R13 <= io_start;
    if(reset) begin
      R26 <= 14'h0;
    end else begin
      R26 <= T49;
    end
    R32 <= T50;
    R44 <= MulSEL_io_o;
  end
endmodule

