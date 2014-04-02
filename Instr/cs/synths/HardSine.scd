Instr("cs.synths.HardSine", {
  arg freq = 440,
    octaveMultiplier = 1,
    gate = 1,
    amp = 1.0,
    sustainDur = 0.25;

  var envShape = Env.new(
      [ 0,    1,    1,    0 ],
      [ 0.001, sustainDur,  0.001    ],
      releaseNode: 2
    ),
    env = EnvGen.ar(envShape, gate, amp, doneAction: 2),
    out;

  out = SinOsc.ar(octaveMultiplier * freq) * env;
  
}, [
  \freq,
  ControlSpec(1, 10, step: 1),
  \gate,
  \amp,
  ControlSpec(0.001, 2.0)
]);
