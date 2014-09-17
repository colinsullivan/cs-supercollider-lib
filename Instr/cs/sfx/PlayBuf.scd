Instr("cs.sfx.PlayBuf", {
  
  arg buf,
    gate = 1,
    playbackRate,
    startTime,
    attackTime,
    releaseTime,
    amp = 1.0,
    convertToStereo = 0;

  var out,
    sustainTime = buf.duration - attackTime - releaseTime;

  out = PlayBuf.ar(
    buf.numChannels,
    buf,
    playbackRate * BufRateScale.kr(buf),
    gate,
    startTime * buf.sampleRate * buf.numChannels,
    1
  );
  //out = out * Linen.ar(gate, attackTime, 1.0, releaseTime, 2);
  out = out * Env.new(
    [ 0.0,        1.0,            1.0,            0.0 ],
    [   attackTime,   sustainTime,    releaseTime     ]
  ).ar(doneAction: 2);

  if (convertToStereo == 1, {
    out = [out, out];    
  });

  amp * out;

}, [
  \buffer,
  \gate,
  // playbackRate
  ControlSpec(-4.0, 4.0),
  // startTime
  ObjectSpec(0.0),
  // attackTime
  ObjectSpec(1.0),
  // releaseTime
  ObjectSpec(1.0),
  \amp
]);
