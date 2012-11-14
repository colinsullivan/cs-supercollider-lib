Instr("fx.FFT.Shuffle", {
  arg in;

  var out,
    spectrum;
  
  spectrum = FFT(LocalBuf(2048), in);
  spectrum = PV_BinScramble(spectrum, MouseY.kr, 0.1, MouseY.kr > 0.1);

  out = IFFT(spectrum);

}, [
  nil // don't know what the spec for another instrument should be
]);
