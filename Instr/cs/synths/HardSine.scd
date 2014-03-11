Instr("cs.synths.HardSine", {
  arg freq = 440,
    gate = 1,
    amp = 1.0;

  var envShape = Env.new(
      [ 0,    1,    1,    0 ],
      [ 0.001, 1.0,  0.001    ]
    ),
    env = EnvGen.ar(envShape, gate, amp, doneAction: 2),
    out;

  out = SinOsc.ar(freq) * env;
  
}, [
  \freq,
  \gate,
  \amp
])
