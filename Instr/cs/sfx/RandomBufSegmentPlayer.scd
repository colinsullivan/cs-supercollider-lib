Instr("cs.sfx.RandomBufSegmentPlayer", {

  arg buf,
    gate = 1,
    playbackRate,
    startTime,
    attackTime,
    endTime,
    releaseTime,
    gain,
    probability,
    convertToStereo = 0;

  Instr.ar("cs.sfx.PlayBufSegment", (
    buf: buf,
    // use probability above to trigger
    gate: CoinGate.kr(probability, gate),
    playbackRate: playbackRate,
    startTime: startTime,
    attackTime: attackTime,
    endTime: endTime,
    releaseTime: releaseTime,
    gain: gain,
    convertToStereo: convertToStereo
  ));

}, [
  \buffer,
  \gate,
  // playbackRate
  ControlSpec(-10, 10),
  // startTime
  ObjectSpec(0.0),
  // attackTime
  ObjectSpec(1.0),
  // endTime (default plays one second of soundfile)
  ObjectSpec(1.0),
  // releaseTime
  ObjectSpec(1.0),
  \gain,
  // probability that we will play
  \unipolar
]);
