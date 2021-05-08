Instr("cs.percussion.OWAKick", {
  arg freq = 110,
    amp = 0.9,
    gate = 0;

  var carrier,
    attackModulator,
    toneModulator,
    out,
    velocity,
    attackPeak,
    velAmp;

  velocity = Latch.kr(gate, gate);

  attackPeak = LinExp.kr(velocity, dstlo: 0.2, dsthi: 1.0);
  velAmp = LinExp.kr(velocity, dstlo: -6.0.dbamp(), dsthi: 1.0);

  attackModulator = Instr.ar("cs.fm.Oscillator", (
    amp: LinExp.kr(velocity, dstlo: 24, dsthi: 50) * freq,
    //amp: 50 * freq,
    freq: 7.3 * freq,
    gate: gate,
    envShape: Env.new(
      [attackPeak,   0.0,    0.0 ],
      [   0.25,    0.75    ],
      [   -3.0,    0.00    ],
      releaseNode: nil
    ),
    //envTimeScale: LinExp.kr(velocity, 1, 0, 0.5, 0.2),
    envTimeScale: 0.2,
    envDoneAction: 0
  ));

  toneModulator = Instr.ar("cs.fm.Oscillator", (
    amp: 40 * freq,
    freq: 0.5 * freq,
    gate: gate,
    envShape: Env.new(
      [0.0,     0.2,    0.0 ],
      [   0.05,      1.0    ],
      [   3.0,     -3.00    ],
      releaseNode: 1
    ),
    envTimeScale: 0.5,
    //envTimeScale: 1,
    envDoneAction: 0
  ));

  carrier = Instr.ar("cs.fm.Oscillator", (
    amp: 1.0,
    freq: freq + attackModulator + toneModulator,
    gate: gate,
    envShape: Env.new(
      //initial attack             peak          exp. decay
      [0.0,   1.0,      0.9,      0.9,      0.0 ],
      [   0.001,      0.05,      0.05,   0.5      ],
      //[   0.001,      0.05,      LinExp.kr(velocity, 0, 1, 0.4, 0.8),   0.5      ],
      [0.0,   2.5,     4.5,      -2.5,   -5.5  ],
      releaseNode: 3
    ),
    envDoneAction: 2
  ));
  out = MidEQ.ar(out, 4.84*1000, 1.4, 6.0);
  out = MidEQ.ar(out, 140, 1.8, -7);
  out = MidEQ.ar(out, 70, 1.5, 12);
  out = amp * carrier * velAmp;

  //out = CompanderD.ar(
    //out,
    //thresh: 0.3,
    //slopeBelow: 1.0,
    //slopeAbove: 0.8,
    //clampTime: 0.005,
    //relaxTime: 0.1
  //);


  //out = [out, out];

}, [
  \freq,
  \amp,
  \gate
]);

