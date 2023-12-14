package com.mj.usecase.exceptions

import com.mj.domain.MemberId

class MemberNotFoundException(id: MemberId) : IllegalArgumentException("존재하지 않는 회원입니다. memberId: $id")