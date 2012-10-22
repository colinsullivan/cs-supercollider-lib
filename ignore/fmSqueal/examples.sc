(
  var fmSqueal = "fmSqueal.sc".resolveRelative.load;

  fmSqueal.add;

  Pbind(
    \instrument, "colinsul-fmSqueal",
    \filterTopFreq,       Pseq([  8000,   16000,   ], 1),
    \carrierBaseFreq,     Pseq([  440,    880,    ], 1),
    \ampEnvAttackTime,    Pseq([  1.0,    0.5,    ], 1),
    \ampEnvReleaseTime,   Pseq([  0.5,    2.0,    ], 1),
    \dur,                 Pseq([  4.0,    4.0,    ], 1)
  ).play;
)
