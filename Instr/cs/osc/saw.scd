Instr("cs.osc.saw", {
  arg freq = 440,
    amp = 1.0;

  Saw.ar(freq, amp);
});
