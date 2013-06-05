Instr("cs.fm.Oscillator", {
  arg freq = 440,
    amp = 1.0,
    gate = 0,
    envShape,
    envTimeScale = 1.0,
    envDoneAction = 0;

  var out,
    env;

  env = EnvGen.ar(
    envelope: envShape,
    gate: gate,
    timeScale: envTimeScale,
    doneAction: envDoneAction
  );
  out = amp * (env * SinOsc.ar(freq));

}, [
  \freq,
  \amp,
  \gate,
  \env,
  \beats
]);
