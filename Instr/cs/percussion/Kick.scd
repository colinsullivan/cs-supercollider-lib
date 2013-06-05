Instr("cs.percussion.Kick", {
  arg freq = 110,
    amp = 0.9,
    gate = 0;

  var carrier,
    attackModulator,
    toneModulator,
    out;

  freq = freq * 0.5;

  attackModulator = Instr.ar("cs.fm.Oscillator", (
    amp: 50 * freq,
    freq: 4.3 * freq,
    gate: gate,
    envShape: Env.new(
      [1.0,   0.0,    0.0 ],
      [   0.25,    0.75    ],
      [   -3.0,    0.00    ],
      releaseNode: nil
    ),
    envTimeScale: 0.12,
    envDoneAction: 0
  ));

  toneModulator = Instr.ar("cs.fm.Oscillator", (
    amp: 40 * freq,
    freq: 1.6 * freq,
    gate: gate,
    envShape: Env.new(
      [0.0,     0.2,    0.0 ],
      [   0.05,      1.0    ],
      [   3.0,     -3.00    ],
      releaseNode: 1
    ),
    //envTimeScale: 0.08,
    envDoneAction: 0
  ));

  carrier = Instr.ar("cs.fm.Oscillator", (
    amp: 1.0,
    freq: freq + attackModulator + toneModulator,
    gate: gate,
    envShape: Env.new(
      //initial attack             peak          exp. decay
      [0.0,   0.75,     0.9,      1.0,      0.9,      0.0 ],
      [   0.001,    0.1,     0.05,      0.05,   1.9      ],
      [0.0,   2.5,      -2.5,     2.5,      -2.5,   -5.5  ],
      releaseNode: 3
    ),
    envDoneAction: 2
  ));

  out = amp * carrier;

  out = [out, DelayC.ar(out, delaytime: 0.05)];

}, [
  \freq,
  \amp,
  \gate
]);
