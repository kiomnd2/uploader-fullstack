import { shallowMount } from '@vue/test-utils';
import ProgressBar from '@/components/ProgressBar';

describe('Dropzone.vue Test', () => {
  let failCount = 5;
  let successCount = 100;

  const wrapper = shallowMount(ProgressBar, {
    propsData: {
      progress: 25,
      successCount: successCount,
      failCount: failCount,
    },
  });

  it('render progressbar', () => {
    expect(wrapper.exists()).toBe(true);
  });

  it('첫번째 프로그레스의 value는 progress의 값과 같다', () => {
    expect(wrapper.find('b-progress').attributes('value')).toBe('25');
  });

  it('두번째 프로그레스의 value는 각각 successCount 와 failCount 값과 같다', () => {
    expect(
      wrapper.findAll('b-progress b-progress-bar').at(0).attributes('value'),
    ).toBe('100');
    expect(
      wrapper.findAll('b-progress b-progress-bar').at(1).attributes('value'),
    ).toBe('5');
  });

  it('maxCount 는 progress 와 같은 비율이다', () => {
    expect(wrapper.vm.maxCount).toBe(420);
  });
});
