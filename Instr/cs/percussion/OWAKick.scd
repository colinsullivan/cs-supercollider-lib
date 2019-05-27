Instr("cs.percussion.OWAKick", {
  arg freq = 110,
    amp = 0.9,
    gate = 0;

  var carrier,
    attackModulator,
    toneModulator,
    out;

  //freq = freq * 0.5;

  attackModulator = Instr.ar("cs.fm.Oscillator", (
    amp: 50 * freq,
    freq: 7.3 * freq,
    gate: gate,
    envShape: Env.new(
      [1.0,   0.0,    0.0 ],
      [   0.25,    0.75    ],
      [   -3.0,    0.00    ],
      releaseNode: nil
    ),
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
      [0.0,   2.5,     4.5,      -2.5,   -5.5  ],
      releaseNode: 3
    ),
    envDoneAction: 2
  ));
  out = amp * carrier;

  out = CompanderD.ar(
    out,
    thresh: 0.3,
    slopeBelow: 1.0,
    slopeAbove: 0.8,
    clampTime: 0.005,
    relaxTime: 0.1
  );


  //out = [out, out];

}, [
  \freq,
  \amp,
  \gate
]);

