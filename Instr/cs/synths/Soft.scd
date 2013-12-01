Instr("cs.synths.Soft.SoftMono", {
  arg freq = 440, gate = 0, amp;

  var carrier,
    modulator,
    ratio,
    out,
    modulatorEnvShape,
    minAttackTime = 0.5,
    maxAttackTime = 2.0,
    attackTime,
    releaseTime = 3.5,
    outEnvShape,
    driftMax,
    flutterMax,
    velocity,
    attackCurve;

  velocity = Latch.kr(gate, gate);

  attackCurve = velocity.range(3.0, 1.0);

  attackTime = velocity.range(maxAttackTime, minAttackTime);
  
  outEnvShape = Env.adsr(
    1.0 * attackTime,
    0.0,
    1.0,
    1.0 * releaseTime,
    1.0,
    [attackCurve, -2, -5]
  );
  modulatorEnvShape = Env.adsr(
    0.2 * attackTime,
    0.8 * attackTime,
    1.0,
    0.8 * releaseTime,
    1.0,
    [attackCurve, -2, -5]
  );

  // subtle analog "drift"
  driftMax = 0.1;
  freq = Rand.new(-1.0 * driftMax, driftMax) + freq;

  // subtle analog "flutter"
  flutterMax = 3;
  freq = SinOsc.ar(Rand.new(10, 100)).range(-1 * flutterMax, flutterMax) + freq;

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
}, [
  \freq,
  \gate,
  \amp
]);

Instr("cs.synths.Soft.SoftSynth", {
  arg freq = 440, gate = 0, amp;

  var out,
    soft,
    softDetuned,
    softDelayed,
    panAmt;

  panAmt = 0.9;

  soft = Instr.ar("cs.synths.Soft.SoftMono", (
    freq: freq,
    gate: gate,
    amp: amp
  ));
  softDetuned = Instr.ar("cs.synths.Soft.SoftMono", (
    freq: freq + Rand.new(-1.0, 1.0),
    gate: gate,
    amp: amp
  ));

  softDelayed = DelayC.ar(soft, 0.1, 0.1);
  
  //out = Pan2.ar(soft, panAmt) + Pan2.ar(softDetuned, 1.0 - panAmt);
  out = Pan2.ar(soft, panAmt) + Pan2.ar(softDelayed, 1.0 - panAmt);

  out;
}, [
  \freq,
  \gate,
  \amp
]);
