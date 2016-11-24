SELECT *
FROM av_avdpool_next.liegenschaften_grundstueck AS g, av_avdpool_next.liegenschaften_liegenschaft AS l
WHERE g.t_id = l.liegenschaft_von

limit 10


SELECT t_id, nbident, identifikator, gueltigkeit, gbeintrag, gueltigereintrag
FROM av_avdpool_next.liegenschaften_lsnachfuehrung


WHERE nbident IN ('SO0200002408')

--ORDER BY gueltigereintrag DESC
