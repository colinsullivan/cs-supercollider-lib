Instr("cs.percussion.Impulsive", {
  arg freq = 440,
    amp = 0.9,
    gate = 0;

  var out,
    imp,
    harms = [
      (
        ratio: 4.875,
        rq: 0.1
      ),
      (
        ratio: 2.5,
        rq: 0.1
      ),
      /*(
        ratio: 2,
        rq: 0.01
      ),*/
      /*(
        ratio: 1.875,
        rq: 0.08
      ),
      (
        ratio: 1.5,
        rq: 0.06
      ),*/
      (
        ratio: 1,
        rq: 0.09
      )
    ];

  // a single impulse
  out = Impulse.ar(0);

  // filtered with resonances at harmonics
  harms.do({
    arg harm;


    out = RLPF.ar(out, freq*harm['ratio'], harm['rq']);
  });

  out = amp * out;

}, [
  \freq,
  \amp,
  \gate
]);
