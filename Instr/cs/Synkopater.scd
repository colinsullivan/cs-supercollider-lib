Instr("cs.Synkopater.SynkopaterVoice", {
  arg gate, delayAmt = 0, tempoBus;

  var out;

  /*out = Instr.ar("cs.percussion.Impulsive", (
    gate: gate,
    doneAction: 0
  ));*/

  var tempo,trig,amp;
  tempo = In.kr(tempoBus);
  trig = Impulse.kr(tempo);
  amp = Decay2.kr(trig,0.01,0.1).clip2(1.0);
  out = amp * SinOsc.ar(300);

  out;
}, [
  \gate,
  ControlSpec(0.0, 10.0),
  \bus
]);
