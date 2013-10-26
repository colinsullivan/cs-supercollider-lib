Instr("cs.synths.Soft", {
  arg freq = 440, gate = 0, amp;

  var carrier,
    modulator,
    ratio,
    out,
    modulatorEnvShape,
    attackTime = 0.75,
    releaseTime = 3.5,
    outEnvShape;

  outEnvShape = Env.adsr(
    1.0 * attackTime,
    0.0,
    1.0,
    1.0 * releaseTime,
    1.0,
    [2, -2, -5]
  );
  modulatorEnvShape = Env.adsr(
    0.2 * attackTime,
    0.8 * attackTime,
    1.0,
    0.8 * releaseTime,
    1.0,
    [2, -2, -5]
  );

  // fundamental is at modulator freq and only odd harmonics are present
  //ratio = (1.0 / 2.0);
  // every third harmonic is missing
  ratio = (1.0 / 3.0);
  
  modulator = Instr.ar("cs.fm.Oscillator", (
    freq: freq,
    gate: gate,
    envShape: modulatorEnvShape
  ));

  carrier = Instr.ar("cs.fm.Oscillator", (
    //freq: freq + (modulator * freq),
    freq: freq + (ratio * freq * modulator),
    gate: gate,
    envShape: outEnvShape
  ));

  
  out = amp * EnvGen.kr(outEnvShape, gate, doneAction: 2) * carrier;

  out;

}, [
  \freq,
  \gate,
  \amp
]);
