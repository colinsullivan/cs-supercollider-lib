/**
 *  @class        StreamBuf
 *
 *  @classdesc    Just like PlayBuf, but streams from disk, always starting
 *  at beginning and a constant rate.
 **/
Instr("cs.sfx.StreamBuf", {
  arg buf,
    gate = 1,
    attackTime = 0.01,
    sustainTime,
    releaseTime = 0.01,
    amp = 1.0,
    convertToStereo = 0,
    isSustained = 0,
    isLooping = 0;

  var out,
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

  out = DiskIn.ar(buf.numChannels, buf.bufnum, loop: isLooping);

  out = out * Env.new(
    [ 0.0,        1.0,            1.0,            0.0 ],
    [   attackTime,   sustainTime,    releaseTime     ],
    loopNode: loopNode,
    releaseNode: releaseNode
  ).ar(gate: gate, doneAction: Done.freeSelf);

  if (convertToStereo == 1, {
    out = [out, out];    
  });

  out = amp * out;

  
}, [
  \buffer,
  \gate,
  // attackTime
  [0.0, 200.0],
  // sustainTime
  [0.0, 1000.0],
  // releaseTime
  [0.0, 200.0],
  \amp,
  // convertToStereo
  [0, 1],
  // isSustained
  [0, 1],
  // isLooping
  [0, 1]
]);
