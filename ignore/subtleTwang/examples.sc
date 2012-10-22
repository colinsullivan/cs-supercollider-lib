(
  var subtleTwang = "subtleTwang.sc".resolveRelative.load,
    base = 220,
    v;

  /*subtleTwang.load;*/
  subtleTwang.miditest();

  /*Pbind(
    \instrument,      "colinsul-subtleTwang",
    \freq,            Pseq([base, base * 2, base * 3, base * 4], 1),
    \dur,             1.0
  ).play;*/

  /*v = Voicer.new(8, "colinsul-subtleTwang");
  VoicerMIDISocket(0, v);*/
)
