Instr("cs.synths.SineBeep", {
  arg freq = 440, amp = 0.5;

  var out,
    envShape;

  envShape = Env.perc(0.01, 0.2);

  out = SinOsc.ar(freq);

  out = EnvGen.ar(envShape, doneAction: 2) * [out, out];
}, [
  \freq,
  \amp
]);
