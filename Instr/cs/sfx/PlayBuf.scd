Instr("cs.sfx.PlayBuf", {
  
  arg buf,
    gate = 1,
    playbackRate = 1.0,
    startTime = 0,
    attackTime = 0.01,
    sustainTime,
    releaseTime = 0.01,
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

  // if we're sustaining, set loop and release nodes
  if (isSustained != 0, {
    loopNode = 1;
    releaseNode = 2;
  });

  out = PlayBuf.ar(
    buf.numChannels,
    buf,
    rate: playbackRate * BufRateScale.kr(buf),
    trigger: gate,
    startPos: startFrame,
    doneAction: 2
  );

  //out = out * Linen.ar(gate, attackTime, 1.0, releaseTime, 2);
  //out = out * Env.new(
    //[ 0.0,        1.0,            1.0,            0.0 ],
    //[   attackTime,   sustainTime,    releaseTime     ],
    //loopNode: loopNode,
    //releaseNode: releaseNode
  //).ar(gate: gate, doneAction: 2);

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
