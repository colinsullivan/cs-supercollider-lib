Instr("cs.fx.HarmonicFilter", {
  arg in,
    freq = 440,
      filterHarms = [
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

  var out;

  out = In.ar(bus, numChan);

  filterHarms.do({
    arg harm;

    out = RLPF.ar(out, freq*harm['ratio'], harm['rq']);
  });

  out;
});
