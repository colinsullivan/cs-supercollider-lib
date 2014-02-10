Instr("cs.Synkopater.SynkopaterVoice", {
  arg gate,
    delayAmt = 0,
    tempoBus,
    phase = 0,
    multiplier = 1;

  var out;

  /*out = Instr.ar("cs.percussion.Impulsive", (
    gate: gate,
    doneAction: 0
  ));*/

  var tempo,trig,amp;
  tempo = In.kr(tempoBus);
  trig = Impulse.kr(tempo * multiplier, phase);
  amp = Decay2.kr(trig,0.01,0.1).clip2(1.0);
  out = amp * SinOsc.ar(300);

  out = DelayC.ar(out, 5.0, delayAmt);

  out;
}, [
  \gate,
  ControlSpec(0.0, 10.0),
  \bus,
  \unipolar,
  ControlSpec(0.0, 8.0)
]);
