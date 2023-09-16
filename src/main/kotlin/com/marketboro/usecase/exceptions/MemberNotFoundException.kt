package com.marketboro.usecase.exceptions

import com.marketboro.domain.MemberId

class MemberNotFoundException(id: MemberId) : IllegalArgumentException("존재하지 않는 회원입니다. memberId: $id")