Instr("fm.Rhodes", {
  arg freq = 440,
    amp = 0.9,
    gate = 0;

  var envShape,
    outEnv,
    out,
    fm1,
    fm2,
    trem;

  fm1 = Instr.ar("fm.BasicFM", (
    freq: freq,
    amp: amp,
    gate: gate,
    carrierFreqRatio: 1.0,
    modulatorFreqRatio: 0.5,
    carrierEnvShape: Env.adsr(
      attackTime: 0.001,
      decayTime: 0.50,
      sustainLevel: 0.7 * amp,
      releaseTime: 0.5,
      peakLevel: amp,
      curve: [4, -4, -2]
    ),
    modEnvShape: Env.adsr(
      attackTime: 0.001,
      decayTime: 0.50,
      sustainLevel: 0.7 * amp,
      releaseTime: 0.5,
      peakLevel: amp
    )
  ));

  fm2 = Instr.ar("fm.BasicFM", (
    freq: freq,
    amp: amp,
    gate: gate,
    carrierFreqRatio: 1.0,
    modulatorFreqRatio: 30,
    carrierEnvShape: Env.adsr(
      attackTime: 0.001,
      decayTime: 0.50,
      sustainLevel: 0.7 * amp,
      releaseTime: 0.5,
      peakLevel: amp,
      curve: [4, -4, -2]
    ),
    modEnvShape: Env.adsr(
      attackTime: 0.001,
      decayTime: 0.1,
      sustainLevel: 0.3 * amp,
      releaseTime: 0.2,
      peakLevel: amp,
      curve: [4, -4, -2]
    )
  ));

  // tremolo
  trem = SinOsc.kr(25.0).range(0.8, 1.0);

  out = (
    trem * 0.5 * fm1 + 
    0.4 * fm2
  );
  /*out = MoogFF.ar(out, 700, 1.25);*/
  /*out = MoogFF.ar(out, MouseX.kr(100, 15000).poll, MouseY.kr(0.1, 5.0).poll);*/
  /*out = LPF.ar(out, MouseX.kr(100, 1500).poll);*/
  out = LPF.ar(out, 1200);

  [out, out];
}, [
  \freq.asSpec(),
  \amp.asSpec(),
  \nil
]);
