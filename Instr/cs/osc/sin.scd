Instr("cs.osc.sin", {
  arg freq = 440,
    amp = 1.0;

  amp * SinOsc.ar(freq);
});
