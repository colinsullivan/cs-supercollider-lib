Instr("cs.sfx.PlayBufWarped", {
  
  arg buf,
    gate = 1,
    playbackRate,
    startTime,
    attackTime,
    releaseTime,
    convertToStereo = 0,
    warpAmp = 0.5,
    warpFreq = 0.5;

  var out,
    rateModulator;


  rateModulator = SinOsc.ar(warpFreq).range(
    playbackRate - warpAmp,
    playbackRate + warpAmp
  );

  out = Instr.ar("cs.sfx.PlayBuf", (
    buf: buf,
    gate: gate,
    playbackRate: rateModulator,
    startTime: startTime,
    attackTime: attackTime,
    releaseTime: releaseTime,
    convertToStereo: convertToStereo
  ));

}, [
  \buffer,
  \gate,
  ObjectSpec(1.0),
  ObjectSpec(0.0),
  ObjectSpec(1.0),
  ObjectSpec(1.0),
  \amp,
  \beats
]);
