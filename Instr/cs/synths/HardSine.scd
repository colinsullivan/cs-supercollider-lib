Instr("cs.synths.HardSine", {
  arg freq = 440,
    gate = 1,
    octaveMultiplier = 1,
    amp = 1.0,
    sustainDur = 0.25,
    sustain = 0;

  var envShape = Env.new(
      [ 0,    1,    1,    0 ],
      [ 0.001, sustainDur,  0.001    ],
      releaseNode: 2
    ),
    env,
    out;

  if (sustain != 0, {
    gate = Trig.kr(gate, sustain);
  });    
  env = EnvGen.ar(envShape, gate, amp, doneAction: 2);
  
  out = SinOsc.ar(octaveMultiplier * freq) * env;
  
}, [
  \freq,
  \gate,
  ScalarSpec(1, 10, step: 1),
  \amp,
  ScalarSpec(0.001, 2.0)
]);
