MetronomeSequencer : GenerativeSequencer {
  var pat;
  initOutputs {
    // define a simple synth
    SynthDef(\simple, {
      arg freq, amp = 0.2;
      var out;
      out = SinOsc.ar(freq, 0, amp) * EnvGen.kr(Env.linen(0.001, 0.05, 0.3), doneAction: 2);
      Out.ar(0, [out, out]);
    }).add();
  }
  initSeqGenerator {

    pat = Pbind(
      // the name of the SynthDef to use for each note
      \instrument, \simple,
      \midinote, Pseq([96, 84, 84, 84], inf),
      // rhythmic values
      \dur, 1
    );

  }

  queue {
    arg clock;
    pat.play(clock: clock, quant: [4]);
  }
}
