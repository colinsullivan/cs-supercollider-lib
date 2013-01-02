Instr("cs.fx.FFT.Shuffle", {
  arg in,
    scrambleAmt = 0.0,
    scrambleWidth = 0.1;

  var out,
    spectrum,
    gate;

  gate = BinaryOpUGen('>', scrambleAmt, 0.1);

  spectrum = FFT(LocalBuf(2048), in);
  spectrum = PV_BinScramble(spectrum, scrambleAmt, scrambleWidth, gate);

  out = IFFT(spectrum);

  out = LPF.ar(out, 1500);

  // output stereo here for now.
  [out, out];

}, [
  nil, // don't know what the spec for another instrument should be
  ControlSpec(0.0, 1.0),
  ControlSpec(0.1, 1.0)
]);
