package kael

func split_by_n(bs []byte, n int) (res [][]byte) {
	fsz := len(bs) / n

	if len(bs) % n != 0 {
		fsz++
	}
	for i := 0; i < fsz; i++ {
		sz := n
		if sz > len(bs) - i * n {
			sz = len(bs) - i * n
		}

		res = append(res, bs[i * n:i * n + sz])
	}

	return
}

