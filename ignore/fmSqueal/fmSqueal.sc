(SynthDef.new("colinsul-fmSqueal", {
  arg carrierBaseFreq = 440,
    filterTopFreq = 12000,
    gate = 1,
    ampEnvAttackTime = 1.0,
    ampEnvReleaseTime = 0.5;
  var carrier,
    modulator,
    filterModulator,
    ampEnv = EnvGen.kr(Env.adsr(
      attackTime: ampEnvAttackTime,
      releaseTime: ampEnvReleaseTime
    ), gate, doneAction: 2),
    glideEnv = EnvGen.kr(Env.perc(
      attackTime: 1.0,
      releaseTime: 0.2
    ), gate),
    carrierFreq;

  modulator = SinOsc.kr(freq: 5.0);

  carrierBaseFreq = glideEnv * 2 * carrierBaseFreq;
  carrierFreq = modulator.range(carrierBaseFreq, carrierBaseFreq * 2);
  carrier = LFSaw.ar(freq: carrierFreq);
  /*filterModulator = SinOsc.kr(freq: 1.0, phase: 3*pi/2.0).range(filterTopFreq, 440);*/
  filterModulator = 440 + (glideEnv * filterTopFreq);
  
  Out.ar([0, 1], ampEnv * MoogFF.ar(in: carrier, freq: filterModulator));
}))
