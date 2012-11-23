
/**
 *  A combination of effects.
 **/
Instr("fx.CircularWarpInput", {
  // the overall amplitude of the effect
  arg amp,
    // circular buffer
    buf,
    playbackRate = 1.0,
    scrambleAmt = 0.0;

  var out;
  
  out = Instr.ar("fx.Circular.Player", (
    buf: buf,
    /*playbackRate: MouseY.kr(0.1, 5.0)*/
    playbackRate: playbackRate
  ));
  out = Instr.ar("fx.FFT.Shuffle", (
    in: out,
    /*scrambleAmt: MouseX.kr(0.25, 1.0)*/
    scrambleAmt: scrambleAmt
  ));

  out = amp * out;

}, [
  // this effect is soft
  ControlSpec(0.0, 2.5),
  \buffer,
  ControlSpec(0.1, 5.0, \exp),
  ControlSpec(0.25, 1.0)
]);
