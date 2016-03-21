Instr("cs.sfx.PlayBuf", {
  
  arg buf,
    gate = 1,
    playbackRate,
    startTime,
    attackTime,
    sustainTime,
    releaseTime,
    amp = 1.0,
    convertToStereo = 0,
    isSustained = 0;

  var out,
    startFrame = ((startTime / buf.duration) * BufFrames.kr(buf)).floor(),
    loopNode = nil,
    releaseNode = nil;

  if (sustainTime == nil, {
    sustainTime = buf.duration - attackTime - releaseTime;
  });

  if (isSustained != 0, {
    loopNode = 1;
    releaseNode = 2;
  });

  out = PlayBuf.ar(
    buf.numChannels,
    buf,
    playbackRate * BufRateScale.kr(buf),
    gate,
    startFrame
  );

  //out = out * Linen.ar(gate, attackTime, 1.0, releaseTime, 2);
  out = out * Env.new(
    [ 0.0,        1.0,            1.0,            0.0 ],
    [   attackTime,   sustainTime,    releaseTime     ],
    loopNode: loopNode,
    releaseNode: releaseNode
  ).ar(doneAction: 2, gate: gate);

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
